
To show the GPS location feature, it's best to use an actual device instead of an emulator.

dependencies {
    testCompile 'junit:junit:4.12'
    compile 'com.android.support:appcompat-v7:23.1.1'
    compile 'com.android.support:design:23.1.1'
    // WeatherLib Library
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.survivingwithandroid:weatherlib:1.6.0'
    compile 'com.survivingwithandroid:weatherlib_volleyclient:1.6.0'
    compile 'com.survivingwithandroid:weatherlib_okhttpclient:1.6.0'
    compile 'com.squareup.okhttp:okhttp:2.0.+'
}
