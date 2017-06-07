## Thread종료

```java
// 플래그를 줘야 앱을 종료했을때 Thread가 종료 됨 그러지 않으면 계속 실행되어 배터리 die....
boolean runFlag = true;

@Override
    protected void onDestroy() {
        super.onDestroy();
        runFlag = false; // thread 종료
    }
```
