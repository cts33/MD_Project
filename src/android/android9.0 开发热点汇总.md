最近调研需要开发Android 9.0的WiFi 热点，要求能够打开和关闭热点，及获取热点信息（名称和密码）。
但是热点模块属于系统级api，第三方应用无法直接获取，还好我的app可以设置为系统级应用，可以直接获取api来执行，
但是如果不是系统级应用，需要通过反射来处理。

### 1.打开wifi热点

```java
 private void startTethering(){
        ConnectivityManager connectivityManager=((ConnectivityManager)context.getSystemService(CONNECTIVITY_SERVICE));
            try{
                Class classOnStartTetheringCallback=Class.forName("android.net.ConnectivityManager$OnStartTetheringCallback");
                Method startTethering=connectivityManager.getClass().getDeclaredMethod("startTethering",int.class,boolean.class,classOnStartTetheringCallback);
                Object proxy=ProxyBuilder.forClass(classOnStartTetheringCallback).handler(new InvocationHandler(){
                @Override
                public Object invoke(Object o,Method method,Object[]objects)throws Throwable{
                                return null;
                            }
                        }).build();
                startTethering.invoke(connectivityManager,0,false,proxy);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
```

该逻辑有个 OnStartTetheringCallback，需要实例化class 对象，这里借助第三方框架来做的。
implementation 'com.linkedin.dexmaker:dexmaker-mockito:2.12.1'

### 2.关闭wifi热点

```java
  private void stopTethering(){
        ConnectivityManager connectivityManager=((ConnectivityManager)context.getSystemService(CONNECTIVITY_SERVICE));
            try{
                Method stopTethering=connectivityManager.getClass().getDeclaredMethod("stopTethering",int.class);
                stopTethering.invoke(connectivityManager,0);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
```
### 3.wifi热点 是否打开

```java
   public boolean isWifiApEnabled(){
        WifiManager wifiManager=(WifiManager)App.getContext().getApplicationContext().getSystemService(
        Context.WIFI_SERVICE);
            try{
                Method method=wifiManager.getClass().getMethod("isWifiApEnabled");
                method.setAccessible(true);
                return(Boolean)method.invoke(wifiManager);
            }catch(NoSuchMethodException e){
                e.printStackTrace();
            }catch(Exception e){
                e.printStackTrace();
            }
            return false;
        }
```

### 4.获取wifi热点信息

```java
 public String getApInfo(){
        WifiManager wifiManager=(WifiManager)App.getContext().getApplicationContext().getSystemService(
        Context.WIFI_SERVICE);
            try{
                Method localMethod=wifiManager.getClass().getDeclaredMethod("getWifiApConfiguration",new Class[0]);
                WifiApConfiguration config = localMethod.invoke(wifiManager);
                String ssid = config.SSID;
                String key = config.preSharedKey;
            }catch(Exception localException){
                localException.printStackTrace();
            }
            return null;
        }
```

在获取getWifiApConfiguration的方法的时候，会提示下面错误，需要添加系统权限
android.Manifest.permission.ACCESS_WIFI_STATE
android.Manifest.permission.OVERRIDE_WIFI_CONFIG

java.lang.SecurityException:App not allowed to read or update stored WiFi Ap config
```java
java.lang.SecurityException:App not allowed to read or update stored WiFi Ap config(uid=1000)
        at android.os.Parcel.readException(Parcel.java:2004)
        at android.os.Parcel.readException(Parcel.java:1950)
        at android.net.wifi.IWifiManager$Stub$Proxy.getWifiApConfiguration(IWifiManager.java:1769)
        at android.net.wifi.WifiManager.getWifiApConfiguration(WifiManager.java:2221)
        at com.android.settings.TetherSettings.initWifiTethering(TetherSettings.java:205)
        at com.android.settings.TetherSettings.onCreate(TetherSettings.java:171)
```

### 5.注册热点的监听器

```java
context.registerReceiver(
        wifiApBroadcastReceiver,IntentFilter(WifiManager.WIFI_AP_STATE_CHANGED_ACTION),
        null,handler
        )
```
### 6.关于如何把普通应用改为系统应用
之前也以为在清单文件里设置android:sharedUserId=“android.uid.system” 就可以改为系统应用，其实不是这样的。
系统app和普通app最大的区别在于签名，系统应用使用rom的签名；如何使用系统签名打包，问百度即可

### 7. 修改系统权限设置

```java
    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
    intent.setData(Uri.parse("package:" + getPackageName()));
    startActivityForResult(intent, REQUEST_CODE_WRITE_SETTINGS );
    
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data)   {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_CODE_WRITE_SETTINGS)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                //Settings.System.canWrite方法检测授权结果
                    if (Settings.System.canWrite(getApplicationContext()))
                    {
                    T.show("获取了权限");
                    } 
                }
            }
        }
```