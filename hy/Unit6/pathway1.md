
## WorkManager
* 상황별 실행과 보장된 실행을 조합하여 적용해야 하는 **백그라운드 작업**을 위한 아키텍처 구성요소


* 장점
  * 비동기 일회성 작업과 주기적인 작업 모두 지원
  * 네트워크 상태, 저장공간, 충전 상태와 같은 제약 조건 지원
  * 동시 작업 실행을 포함한 복잡한 작업 요청 체이닝
  * 한 작업 요청의 출력이 다음 작업 요청의 입력으로 사용됨
  * 하위 버전인 API 수준 14와 호환성 처리
  * Google Play 서비스 사용 여부와 관계없이 작동함
  * 시스템 상태 권장사항 준수
  * UI에 작업 요청 상태를 쉽게 표시하는 LiveData 지원

<br><br>

* 적합한 작업
  * 로그 업로드
  * 이미지에 필터 적용 및 저장
  * 주기적인 로컬 데이터를 네트워크와 동기화



<br><br><br>

### WorkManager 추가
```gradle
// build.gradle(module)
dependencies {
    // Other dependencies
    implementation "androidx.work:work-runtime-ktx:$versions.work"
}


//build.gradle(project)
buildscript {
    // Other versions
    versions.work = "2.5.0"
}
```




<br><br><br>

### WordManager 클래스
* `Worker`
  * 백그라운드에서 실행하고자 하는 실제 작업의 코드 입력
  * 클래스 확장 후 `doWork()` 메서드 재정의
  
<br>

* `WorkRequest`
  * 작업 실행 요청 표현
  * WorkRequest를 만드는 과정에서 Worker 전달
  * Worker를 실행할 시점에 적용되는 `Constraints` 등 지정 가능

<br>

* `WorkManager`
  * 실제롤 WorkRequest를 예약하고 실행
  * 지정된 제약조건을 준수하며 시스템 리소스에 부하를 분한하는 방식으로 WorkRequest 예약


<br><br><br>

### Worker 클래스 생성
1. workers 패키지 > BlurWorker 클래스 생성 > 클래스에 `Worker` 종속항목 추가
2. `doWork()` 재정의
3. `applicationContext` 속성을 호출하여 context를 가져옴 : 비트맵 조작에 사용
4. WorkerUtils 의 메서드를 호출하여 비트맵 조작
5. try - catch 로 `Result.success()` / `Result.failure()` 반환
6. viewModel 클래스에서 WorkManager 인스턴스 변수 생성
7. viewModel 클래스에서 WorkManager를 사용해 WorkRequest 를 큐에 추가
   `workManager.enqueue(OneTimeWorkRequest.from(BlurWorker::class.java))`

```kotlin
class BlurWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    override fun doWork(): Result {                             //2
        val appContext = applicationContext                     //3

        makeStatusNotification("Blurring image", appContext)    //4

        return try {                                            //5
            val picture = BitmapFactory.decodeResource(
                    appContext.resources,
                    R.drawable.test)

            val output = blurBitmap(picture, appContext)        //4

            // Write bitmap to a temp file
            val outputUri = writeBitmapToFile(appContext, output)   //4

            makeStatusNotification("Output is $outputUri", appContext)

            Result.success()
        } catch (throwable: Throwable) {
            Timber.e(throwable, "Error applying blur")
            Result.failure()
        }
    }
}
```

<br><br>

> 임시파일 확인 <br>
> 안드로이드 스튜디오 > View > Tool Windows > Device File Explorer <br>
> data > data > com.example.background > files > blur_filter_outputs > URI 선택  <br>
> 목록 새로고침 : 우클릭 > Synchronize 


<br><br><br>

### 입출력 추가
* `Data` 객체 : 키 - 값 쌍의 경량 컨테이너. 입출력 전달
  
1. viewModel 클래스에서 `Data.Builder` 객체 생성 
2. `putString()` 메서드로 키, 값을 전달하여 imageUri를 Data 객체에 추가
3. `Data.Builder` 객체의 `build()` 메서드로 객체 반환
4. Data 객체를 전달할 `OneTimeWorkRequest.Builder` 생성
5. `setInputData()` 메서드로 3의 반환 결과 전달 > `build()`
6. WorkManager로 요청을 큐에 추가
7. BlurWorker 클래스에서 doWork에서 Data 객체 가져오기

```kotlin
class BlurViewModel(application: Application) : ViewModel() {
    ...
    private fun createInputDataForUri(): Data {                 
        val builder = Data.Builder()                                //1
        imageUri?.let {
            builder.putString(KEY_IMAGE_URI, imageUri.toString())   //2
        }
        return builder.build()                                      //3
    }

    internal fun applyBlur(blurLevel: Int) {
        val blurRequest = OneTimeWorkRequestBuilder<BlurWorker>()   //4
                .setInputData(createInputDataForUri())              //5
                .build()

        workManager.enqueue(blurRequest)                            //6
    }
    ...
}
```


```kotlin
// BlurWorker.kt
override fun doWork(): Result {
    val appContext = applicationContext

    val resourceUri = inputData.getString(KEY_IMAGE_URI)        //7

    makeStatusNotification("Blurring image", appContext)

    return try {
        if (TextUtils.isEmpty(resourceUri)) {
            Timber.e("Invalid input uri")
            throw IllegalArgumentException("Invalid input uri")
        }

        val resolver = appContext.contentResolver

        val picture = BitmapFactory.decodeStream(
                resolver.openInputStream(Uri.parse(resourceUri)))

        val output = blurBitmap(picture, appContext)

        // Write bitmap to a temp file
        val outputUri = writeBitmapToFile(appContext, output)
        val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())
        Result.success(outputData)
    
    } catch (throwable: Throwable) {
        Timber.e(throwable)
        Result.failure()
    }
}
```
<br><br><br>

### 작업 체이닝
1. 임시파일을 정리하는 `CleanupWorker` 클래스 생성
2. 이미지 파일을 저장하는 `SaveImageToFileWorker` 클래스 생성 : 입출력 처리
3. viewModel 클래스 > applyBlur 메서드에서 workManager.enqueue() 대신 `workManager.beginWith()` 호출 : `WorkContinuation`이 반환됨
4. `then()` 메서드로 체인 추가

 
```kotlin
//viewModel
internal fun applyBlur(blurLevel: Int) {
    // Add WorkRequest to Cleanup temporary images
    var continuation = workManager
            .beginWith(OneTimeWorkRequest           //3
            .from(CleanupWorker::class.java))

    // Add WorkRequest to blur the image
    val blurRequest = OneTimeWorkRequest.Builder(BlurWorker::class.java)
            .setInputData(createInputDataForUri())
            .build()

    continuation = continuation.then(blurRequest)   //4

    // Add WorkRequest to save the image to the filesystem
    val save = OneTimeWorkRequest.Builder(SaveImageToFileWorker::class.java).build()

    continuation = continuation.then(save)          //4

    // Actually start the work
    continuation.enqueue()
}
```
<br><br>

> WorkRequest 에 작업 요청 체인 추가
> ```kotlin
> val continuation = workManager.beginWith(workA)
> continuation.then(workB) 
>       .then(workC)
>       .enqueue() 
> ``` 


<br><br><br>

### 고유 작업 체인
* 작업 체인을 한번에 하나씩 실행해야 하는 경우
* `beginUniqueWork(String, ExistingWorkPolicy, OneTimeWorkRequest)` 메서드 사용
* ExistingWorkPolicy
  * REPLACE : 기존 작업을 취소하고 새 작업 실행
  * KEEP : 기존 작업을 유지하고 새 작업 무시
  * APPEND : 기존 작업을 완료한 후 새 작업 실행 (기존 작업에 새 작업 추가)

```kotlin
//viewModel - applyBlur
var continuation = workManager
        .beginUniqueWork(
                IMAGE_MANIPULATION_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.from(CleanupWorker::class.java)
        )
```


<br><br><br>


### 태그 지정, 작업 상태 표시
* WorkInfo 
  * WorkRequest의 id, 상태, 출력, 태그, 실행시도 횟수 등 세부정보에 대한 객체
  * WorkInfo 객체가 포함된 liveData를 통해 상태 확인 가능

* 작업상태
  * BLOCKED : WorkRequest 필수 구성요소가 성공적으로 완료되지 않아 현재 차단됨
  * CANCELLED : WorkRequest가 취소됨. 실행되지 않음
  * ENQUEUED : WorkRequest가 대기 상태며 제약조건이 충족되고 리소스가 사용가능할 때 실행할 수 있음
  * FAILED : WorkRequest가 실패됨
  * RUNNING : WorkRequest가 현재 실행 중임
  * SUCCEEDED : WorkRequest가 성공적으로 완료됨

<br><br>

 * WorkInfo 객체 가져오기 : `LiveData<WorkInfo>` / `LiveData<List<WorkInfo>>`
  1. ID 사용
      * WorkManager 메서드 : getWorkInfoByIdLiveData
      * 각 WorkRequest는 WorkManager에서 생성된 고유 id 가 있음.<br>
        id를 사용하여 WorkRequest의 단일 LiveData<WorkInfo>를 얻을 수 있음.

<br>

  2. 고유체인 이름 사용
      * WorkManager 메서드 : getWorkInfosForUniqueWorkLiveData
      * WorkRequest는 고유 체인에 포함됨. <br>
        메서드는 고유한 단일 WorkRequests 체인에 있는 모든 작업의 LiveData<List<WorkInfo>> 를 반환함.

<br>

  3. 태그 사용
      * WorkManager 메서드 : getWorkInfosByTagLiveData
      * 선택적으로 WorkRequest를 String으로 태그 지정 가능. <br>
        동일한 태그를 사용하여 여러 WorkRequest 연결 가능. <br>
        메서드는 단일 태그의 LiveData<List<WorkInfos>>를 반환함.

<br><br><br>


### 태그 사용
1. viewModel 에서 WorkRequest를 만들 때 `addTag(TAG_OUTPUT)`을 사용해 작업에 태그 지정
2. LiveData<List<WorkInfo>> 변수 생성
3. viewModel 에서 `WorkManager.getWorkInfosByTagLiveData`를 사용해 WorkInfo를 가져오는 init 블록 추가
4. BlurActivity > onCreate 에서 리스트의 첫번째 WorkInfo를 가져옴
5. WorkInfo 관찰자에서 `workInfo.state().isFinished()`를 사용하여 작업이 완료 상태인지 확인
6. 완료되지 않은 경우 `showWorkInProgress()`를 호출하여 적절한 뷰를 숨기고 표시
7. 완료된 경우 `showWorkFinished()`를 호출하여 적절한 뷰를 숨기고 표시

```kotlin
//viewModel
private val workManager = WorkManager.getInstance(application)      //2
    
init {                                                              //3        
    outputWorkInfos = workManager.getWorkInfosByTagLiveData(TAG_OUTPUT)
}

internal fun applyBlur(blurLevel: Int) {
    ...
    val save = OneTimeWorkRequestBuilder<SaveImageToFileWorker>()
        .addTag(TAG_OUTPUT)                                         //1  
        .build()
    ...
}
```

```kotlin
//BlurActivity
override fun onCreate(savedInstanceState: Bundle?) {
    ...
    viewModel.outputWorkInfos.observe(this, workInfosObserver())    
    ...
}

private fun workInfosObserver(): Observer<List<WorkInfo>> {
    return Observer { listOfWorkInfo ->
        if (listOfWorkInfo.isNullOrEmpty()) { return@Observer }

        val workInfo = listOfWorkInfo[0]                            //4

        if (workInfo.state.isFinished) { showWorkFinished() }       //5,7
        else { showWorkInProgress() }                               //6
    }
}
```

<br><br><br>

### 최종 출력 표시 : 최종 이미지 보기
* WorkInfo - `getOutputData()` : 저장된 최종 이미지가 있는 출력 Data 객체를 가져올 수 잇음

1. BlurActivity - outputButton 에 클릭 리스너 설정 : URI를 가져온 후 보는 활동
2. WorkInfo 관찰자에서 WorkInfo 가 완료된 경우 `workInfo.outputData` 를 사용하여 출력 데이터에서 출력 URI 를 가져옴 : `Constants.KEY_IMAGE_URI` 키 사용
3. URI가 null이 아니라면 `viewModel.setOutputUri` 호출

```kotlin
//BlurActivity
override fun onCreate(savedInstanceState: Bundle?) {
    ...
    binding.seeFileButton.setOnClickListener {                      //1
        viewModel.outputUri?.let { currentUri ->
            val actionView = Intent(Intent.ACTION_VIEW, currentUri)
            actionView.resolveActivity(packageManager)?.run {
                startActivity(actionView)
            }
        }
    }
    ...
}

private fun workInfosObserver(): Observer<List<WorkInfo>> {
    return Observer { listOfWorkInfo ->
        if (listOfWorkInfo.isNullOrEmpty()) { return@Observer }

        val workInfo = listOfWorkInfo[0]

        if (workInfo.state.isFinished) {
            showWorkFinished()
            val outputImageUri = workInfo.outputData.getString(KEY_IMAGE_URI)   //2

            if (!outputImageUri.isNullOrEmpty()) {                  //3
                viewModel.setOutputUri(outputImageUri as String)
                binding.seeFileButton.visibility = View.VISIBLE
            }
        } else { showWorkInProgress() }
    }
}
```


<br><br><br>

### 작업 취소
* WorkManager를 사용하여 id, 태그, 고유체인으로 작업 취소 가능

1. viewModel 에서 고유체인 이름으로 작업 취소 메서드 생성
2. BlurActivity 에서 viewModel의 작업취소 메서드 호출. cancleButton 클릭리스너 설정


 
```kotlin
//viewModel
internal fun cancelWork() {
    workManager.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME)
}

//BlurActivity - onCreate
binding.cancelButton.setOnClickListener { viewModel.cancelWork() }
```

<br><br><br>

### 작업 제약조건 (Constraints)
1. viewModel 에서 `Constraints.Builder()`로 Constraints 객체 생성
2. 제약조건 메서드 설정 후 build()
3. WorkRequest에 제약조건 설정

```kotlin
//viewModel - applyBlur
val constraints = Constraints.Builder()             //1
        .setRequiresCharging(true)                  //2
        .build()

val save = OneTimeWorkRequestBuilder<SaveImageToFileWorker>()
        .setConstraints(constraints)                //3
        .addTag(TAG_OUTPUT)
        .build()
...
continuation = continuation.then(save)
continuation.enqueue()
``` 

  
<br><br><br>
<hr>
<br>


### <퀴즈>

* WorkManager <br>
a. 웹 서비스에 대한 GET 요청을 수행하지 않음 <br>



* PeriodicWorkRequest() <br>
a. 지정된 간격 후에 작업을 반복할 때 사용하는 메서드 <br>


* doWork() <br>
a. Worker 클래스의 서브 클래스로 WorkRequest에 의해 실행될 코드 정의  <br>

 