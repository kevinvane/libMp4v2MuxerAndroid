# MP4V2 Native Library

### 导入

Add it in your root `build.gradle` at the end of repositories:
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
        implementation 'com.github.kevinvane:Mp4v2MuxerAndroid:1.0.0'
}
```


### 混淆

```
-keepclassmembers class cn.mp4v2.muxer.** {*;}
```


### 示例

无