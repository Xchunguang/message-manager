## ��������
- �����������ж���ֻ��������ԾͿ����ж���ֻ������ǲ����ܶ�һֱ������ߣ�����Ҫ���յ��ֻ����Ķ��š�����һЩ���Ƶ�APP�������߾�����Щ����̤ʵ����Щ�����Ϣ�洢��δ֪�ƶˣ�������ߣһ����ʵ�õĶ������֡�
- ���ܣ��������Ž��գ������������ʼ�����ʽ���͵����õ������С���������ͽ�������ȫ���ɱ������ã�ȫ������ֻ�з����ʼ����������硣
- ���ú�RN��ذ汾��
  - react-native��0.55.2
  - react��16.3.1
  - react-native-elements��0.19.1  UI
  - react-native-vector-icons��4.6.0
  - react-native-simple-store��1.2.0 �洢
  - react-native-router-flux��3.39.2 ·�ɣ�<a href="https://www.npmjs.com/package/react-native-router-flux#v4-features">V4</a>�汾����react-navigationʵ��
  - alt:0.18.6 ״̬����
  - android-mail:1.5.5/android-activation:1.5.5  �����ʼ�
- �����������̣�
  - ��װ������
    
      cd message-manager
      npm install
      
  - ֱ��������
  
      cd message-manager
      react-native run-android
      
  - ����java��
  
      cd message-manager
      npm start
      
      ʹ��Android Studio��message-manager/android ��Ϊ��Ŀ
      ���debug ѡ��һ��device
      
      
- �������
  - ����jdk/binĿ¼ִ������������Կ���ļ���
    
      keytool -genkey -v -keystore my-release-key.keystore -alias my-key-alias -keyalg RSA -keysize 2048 -validity 10000
      //��Ҫ������Կ��Ͷ�Ӧ���룬���÷�����Ϣ��ע��-alias��ļ�¼������
      
  - ��my-release-key.keystore�ļ��ŵ��㹤���е�android/app�ļ����¡�
  - �༭/message-manager/android/gradle.properties������������ݣ�ע�������Կ������Ϊ�������ɵ���Կ�������
  
      MYAPP_RELEASE_STORE_FILE=my-release-key.keystore
      MYAPP_RELEASE_KEY_ALIAS=my-key-alias
      MYAPP_RELEASE_STORE_PASSWORD=*****
      MYAPP_RELEASE_KEY_PASSWORD=*****
      
  - �༭/message-manager/android/app/build.gradle��������£�
  
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
      
  - ִ����������apk��
  
      cd /message-manager/android
      ./gradlew assembleRelease
      //�����window�¿���ȥ��./ ������gradlew assembleRelease
      
- ʹ������
  - ��Ȩ
  - ���÷������䡣��Ҫ�ʼ�����smtp��ֻ������qq������163����
  - ���ý�������
  - ȡ��ʡ����Եȣ����ֳ���������С�