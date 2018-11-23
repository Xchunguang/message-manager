## 短信助手
- 背景：笔者有多个手机卡，所以就可能有多个手机，但是不可能都一直带在身边，但又要接收到手机卡的短信。网上一些类似的APP，但笔者觉得有些并不踏实，有些会把信息存储在未知云端，所以手撸一个简单实用的短信助手。
- 功能：监听短信接收，并将短信以邮件的形式发送到设置的邮箱中。发送邮箱和接收邮箱全部由本人设置，全部操作只有发送邮件会请求网络。
- 引用和RN相关版本：
  - react-native：0.55.2
  - react：16.3.1
  - react-native-elements：0.19.1  UI
  - react-native-vector-icons：4.6.0
  - react-native-simple-store：1.2.0 存储
  - react-native-router-flux：3.39.2 路由，<a href="https://www.npmjs.com/package/react-native-router-flux#v4-features">V4</a>版本基于react-navigation实现
  - alt:0.18.6 状态管理
  - android-mail:1.5.5/android-activation:1.5.5  发送邮件
- 启动调试流程：
  - 安装依赖：
    
      cd message-manager
      npm install
      
  - 直接启动：
  
      cd message-manager
      react-native run-android
      
  - 调试java：
  
      cd message-manager
      npm start
      
      使用Android Studio打开message-manager/android 作为项目
      点击debug 选择一个device
      
      
- 打包流程
  - 进入jdk/bin目录执行命令生成密钥库文件：
    
      keytool -genkey -v -keystore my-release-key.keystore -alias my-key-alias -keyalg RSA -keysize 2048 -validity 10000
      //需要输入密钥库和对应密码，设置发行信息。注意-alias后的记录别名。
      
  - 把my-release-key.keystore文件放到你工程中的android/app文件夹下。
  - 编辑/message-manager/android/gradle.properties，添加如下内容：注意更改密钥库密码为自已生成的密钥库的密码
  
      MYAPP_RELEASE_STORE_FILE=my-release-key.keystore
      MYAPP_RELEASE_KEY_ALIAS=my-key-alias
      MYAPP_RELEASE_STORE_PASSWORD=*****
      MYAPP_RELEASE_KEY_PASSWORD=*****
      
  - 编辑/message-manager/android/app/build.gradle，添加如下：
  
      ...
      android {
          ...
          defaultConfig { ... }
          signingConfigs {
              release {
                  if (project.hasProperty('MYAPP_RELEASE_STORE_FILE')) {
                      storeFile file(MYAPP_RELEASE_STORE_FILE)
                      storePassword MYAPP_RELEASE_STORE_PASSWORD
                      keyAlias MYAPP_RELEASE_KEY_ALIAS
                      keyPassword MYAPP_RELEASE_KEY_PASSWORD
                  }
              }
          }
          buildTypes {
              release {
                  ...
                  signingConfig signingConfigs.release
              }
          }
      }
      ...
      
  - 执行命令生成apk：
  
      cd /message-manager/android
      ./gradlew assembleRelease
      //如果在window下可以去点./ ，即：gradlew assembleRelease
      
- 使用流程
  - 授权
  - 设置发送邮箱。需要邮件设置smtp，只适配了qq邮箱与163邮箱
  - 设置接收邮箱
  - 取消省电策略等，保持程序持续运行。