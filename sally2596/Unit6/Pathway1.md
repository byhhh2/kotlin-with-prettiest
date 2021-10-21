# Pathway1

### WorkManager

- 지연 가능한 백그라운드 작업 지원
- Android 권장 작업 스케줄러
- 지연 가능한 작업을 실행
- 첫번째 WorkRequest 만들기
    1. Worker 생성 
        
        `BlurWorker` : 이미지를 블러 처리함
        
        - doWork() 정의
            
            ```kotlin
            class BlurWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
            
                override fun doWork(): Result {
                    val appContext = applicationContext
            
                    makeStatusNotification("Blurring image", appContext)
            
                    return try {
                        val picture = BitmapFactory.decodeResource(
                                appContext.resources,
                                R.drawable.test)
            
                        val output = blurBitmap(picture, appContext)
            
                        // Write bitmap to a temp file
                        val outputUri = writeBitmapToFile(appContext, output)
            
                        makeStatusNotification("Output is $outputUri", appContext)
            
                        Result.success()
                    } catch (throwable: Throwable) {
                        Timber.e(throwable, "Error applying blur")
                        Result.failure()
                    }
                }
            }
            ```
            
            - Context 가져오기 ← 비트맵 조작을 위해 필요함
            - `WorkUtils` 에서
                - `blurBitmap` (비트맵 블러 메서드)를 호출 & 블러 비트맵 가져오기
                - `writeBitmapToFile` 호출 & 블러된 비트맵을 임시 파일에 씀
        - ViewModel에서 WorkManager 가져오기
            
            ViewModel에서 WorkManager 인스턴스 변수 생성
            
            ```kotlin
            private val workManager = WorkManager.getInstance(application)
            ```
            
        - WorkManager에서 WorkRequest를 큐에 추가
            
            적용 버튼 누를때 한 번 블러 실행이므로 OneTimeWorkRequest를 큐에 추가
            
            ```kotlin
            Internal fun applyBlur(blurLevel: Int) {
               workManager.enqueue(OneTimeWorkRequest.from(BlurWorker::class.java))
            }
            ```
            
- 입력 및 출력 추가
    
    사용자가 원하는 이미지를 불러오도록 해야함
    
    1. Data 입력 객체 만들기 (사용자가 넣을 이미지 Data)
        
        사용자 이미지를 URI 번들로 전달
        
        `createInputDataForUri` 메서드 작성
        
        ```kotlin
        private fun createInputDataForUri(): Data {
            val builder = Data.Builder()
            imageUri?.let {
                builder.putString(KEY_IMAGE_URI, imageUri.toString())
            }
            return builder.build()
        }
        ```
        
        - `Data.Builder` 객체를 생성함
        - 사용자가 불러온 이미지가 있다면 Data객체에 추가함
            
            → `build()`를 호출하여 Data 객체 생성 및 반환
            
    2. WorkRequest에 Data 객체 전달
        
        ```kotlin
        internal fun applyBlur(blurLevel: Int) {
            val blurRequest = OneTimeWorkRequestBuilder<BlurWorker>()
                    .setInputData(createInputDataForUri())
                    .build()
        
            workManager.enqueue(blurRequest)
        }
        ```
        
        - `OneTimeWorkRequest.Builder` 만들기
        - `setInputData`를 통해 `createInputDataForUri` 결과 전달
        - `OneTimeWorkRequest` 빌드
        - WorkManager를 사용해서 request를 큐에 추가함
    3. 사용자가 선택한 이미지 Data객체를 BlurWorker의 doWork에서 사용하도록 바꾸기
        
        ```kotlin
        override fun doWork(): Result {
            val appContext = applicationContext
        
            // ADD THIS LINE
            val resourceUri = inputData.getString(KEY_IMAGE_URI)
        
            // ... rest of doWork()
        }
        ```
        
    4. 사용자 이미지 Data로 Blur 처리 성공한 것을 Result로 내보내기
        
        ```kotlin
        val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())
        
        Result.success(outputData)
        ```
        
- 작업 체이닝
    1. 블러 처리된 이미지 저장용 Worker와 임시 이미지 파일을 정리하는 Worker 구성하기
        - 이미지 저장 Worker : `SaveImageToFileWorker`
        - 임시 파일 정리 Worker : `CleanupWorker`
    2. `CleanupWorker` 의 doWork() 구현
        
        ```kotlin
        override fun doWork(): Result {
                // Makes a notification when the work starts and slows down the work so that
                // it's easier to see each WorkRequest start, even on emulated devices
                makeStatusNotification("Cleaning up old temporary files", applicationContext)
                sleep()
        
                return try {
                    val outputDirectory = File(applicationContext.filesDir, OUTPUT_PATH)
                    if (outputDirectory.exists()) {
                        val entries = outputDirectory.listFiles()
                        if (entries != null) {
                            for (entry in entries) {
                                val name = entry.name
                                if (name.isNotEmpty() && name.endsWith(".png")) {
                                    val deleted = entry.delete()
                                    Timber.i("Deleted $name - $deleted")
                                }
                            }
                        }
                    }
                    Result.success()
                } catch (exception: Exception) {
                    Timber.e(exception)
                    Result.failure()
                }
            }
        ```
        
    3. `SaveImageToFileWorker` 의 doWork() 구현
        
        ```kotlin
        class SaveImageToFileWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
        
            private val Title = "Blurred Image"
            private val dateFormatter = SimpleDateFormat(
                    "yyyy.MM.dd 'at' HH:mm:ss z",
                    Locale.getDefault()
            )
        
            override fun doWork(): Result {
                // Makes a notification when the work starts and slows down the work so that
                // it's easier to see each WorkRequest start, even on emulated devices
                makeStatusNotification("Saving image", applicationContext)
                sleep()
        
                val resolver = applicationContext.contentResolver
                return try {
                    val resourceUri = inputData.getString(KEY_IMAGE_URI)
                    val bitmap = BitmapFactory.decodeStream(
                            resolver.openInputStream(Uri.parse(resourceUri)))
                    val imageUrl = MediaStore.Images.Media.insertImage(
                            resolver, bitmap, Title, dateFormatter.format(Date()))
                    if (!imageUrl.isNullOrEmpty()) {
                        val output = workDataOf(KEY_IMAGE_URI to imageUrl)
        
                        Result.success(output)
                    } else {
                        Timber.e("Writing to MediaStore failed")
                        Result.failure()
                    }
                } catch (exception: Exception) {
                    Timber.e(exception)
                    Result.failure()
                }
            }
        }
        ```
        
    4. BlurWorker 알림 수정
    5. WorkRequest 체인 만들기
        
        ![https://developer.android.com/codelabs/android-workmanager/img/54832b34e9c9884a.png?hl=ko](https://developer.android.com/codelabs/android-workmanager/img/54832b34e9c9884a.png?hl=ko)
        
        이게 체인이고 블러 처리를 한다는게 이 체인을 만들어서 실행한다는 것
        
    
- 고유 작업 보장
    
    내용의 동기화때문에 한번에 여러 작업 체인이 실행되면 안될 경우
    
    → **고유 작업 체인**
    
    ```kotlin
    var continuation = workManager
            .beginUniqueWork(
                    IMAGE_MANIPULATION_WORK_NAME,
                    ExistingWorkPolicy.REPLACE,
                    OneTimeWorkRequest.from(CleanupWorker::class.java)
            )
    ```
    
    - beginWith 대신 beginUniqueWork 사용
        - 인수
            - ExistingWorkPolicy (아마 기존에 있는거 어떻게 처리할까 인듯)
                - REPLACE
                - KEEP
                - APPEND
- 태그 지정 및 작업 상태 표시
    - WorkInfo : WorkRequest 의 현재 상태에 관한 세부정보
        - 작업의 상태
            - BLOCKED
            - CANCELLED
            - ENQUEUED
            - FAILED
            - RUNNING
            - SUCCEEDED
    - LiveData를 가져오는 방법
        - ID 사용
            - `getWorkInfoByIdLiveData`
            - 각 WorkRequest는 WorkManager에서 생성된 고유 ID가 있음
        - 고유체인이름을 사용
            - `getWorkInfosForUniqueWorkLiveData`
            - 고유한 단일 WorkRequests 체인에 있는 모든 LiveData 반환
        - 태그 사용
            - `getWorkInfosForUniqueWorkLiveData`
            - string으로 WorkRequest 태그를 지정할 수 있음
                
                ```kotlin
                val save = OneTimeWorkRequestBuilder<SaveImageToFileWorker>()
                        .addTag(TAG_OUTPUT) // <-- ADD THIS
                        .build()
                ```
                
- 최종 출력 표시
    1. SeeFile 버튼 표시
        
        ```kotlin
        binding.seeFileButton.setOnClickListener {
             viewModel.outputUri?.let { currentUri ->
                 val actionView = Intent(Intent.ACTION_VIEW, currentUri)
                 actionView.resolveActivity(packageManager)?.run {
                     startActivity(actionView)
                 }
            }
        }
        ```
        
    2. 출력 이미지 표시하기
- 작업 취소
    1. 작업 취소 work만들기
        
        ```kotlin
        internal fun cancelWork() {
            workManager.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME)
        }
        ```
        
    2. 취소 메서드 호출
        
        ```kotlin
        binding.cancelButton.setOnClickListener { viewModel.cancelWork() }
        ```
        
- 작업 제약 조건
    1. 제약 객체 만들기
        
        Constraints.Builder 사용
        
        ```kotlin
        val constraints = Constraints.Builder()
                .setRequiresCharging(true)
                .build() // 객체 빌드
        
        // Add WorkRequest to save the image to the filesystem
        val save = OneTimeWorkRequestBuilder<SaveImageToFileWorker>()
                .setConstraints(constraints) //제약조건 설정
                .addTag(TAG_OUTPUT) //태그 추가
                .build()
        continuation = continuation.then(save)
        
        // Actually start the work
        continuation.enqueue()
        ```