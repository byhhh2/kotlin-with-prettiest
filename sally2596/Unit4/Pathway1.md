# Pathway1

- Thread
    - currentThread()
        
        Thread[Thread-0,5,main]
        
        스레드의 이름, 우선순위, 스레드 그룹
        
    
    - 각 앱에는 전용 스레드, 앱의 UI를 담당하는 스레드가 있음
        
        (경우에 따라 UI 스레드와 기본 스레드가 다를 수 있습니다.)
        
    - 휴대전화는 UI 업데이트를 초당 60회~120회(최소 60회) 시도

- 코루틴
    - 멀티태스킹을 지원
    - 스레드로 작업하는 것보다 다른 수준의 추상화 제공
    - 상태 저장 & 중단 및 재개 가능
    
    [제목 없음](https://www.notion.so/73caa9a8281b40598bb9129206d38b1a)
    
    - async()
        
        ```kotlin
        Fun CoroutineScope.async() {
            context: CoroutineContext = EmptyCoroutineContext,
            start: CoroutineStart = CoroutineStart.DEFAULT,
            block: suspend CoroutineScope.() -> T
        }: Deferred<T>
        ```
        
        Deferred 유형의 값을 반환
        
        Deferred
        
        미래 값 참조를 보유할 수 있는 취소 가능한 Job
        
        Promise나 Future과 같은 것
        
    
    - suspend 함수
        
        일시정지 되거나 재개되는 함수에는 suspend가 붙어야함
        
        만약 함수 내에서 suspend를 사용하면 해당 함수도 suspend가 붙어야함
        
    
    💥선영 정리 
    
    async()는 먼저 실행하긴 하는데 미래 값 참조를 반환하는 함수
    
    그래서 미래값 참조가 언제 가능한지 모르니까 우선 실행하고 값을 찐 참조할때는 await로 기다렸다가 실행한다