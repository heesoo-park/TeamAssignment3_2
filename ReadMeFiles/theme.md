theme
=
## Styles
각 화면은 styles.xml파일을 통해 폰트와 색지정을 하고 있다.
```xml
<resources>
    <style name="Theme.TeamSns" parent="Theme.AppCompat.Light.NoActionBar">
        <!-- Customize your theme here. -->
        <item name="android:textViewStyle">@style/customTextViewFontStyle</item>
        <item name="android:editTextStyle">@style/customEditTextFontStyle</item>
        <item name="android:buttonStyle">@style/customButtonFontStyle</item>
    </style>

    <style name="customTextViewFontStyle" parent="@android:style/Widget.DeviceDefault.TextView">
        <item name="android:fontFamily">@font/custom_font_family</item>
        <item name="android:textColor">@color/text_color</item>
    </style>
    <style name="customButtonFontStyle" parent="@android:style/Widget.DeviceDefault.Button">
        <item name="android:fontFamily">@font/custom_font_family</item>
        <item name="android:textColor">@color/text_color</item>
    </style>

    <style name="customEditTextFontStyle" parent="@android:style/Widget.DeviceDefault.EditText">
        <item name="android:fontFamily">@font/custom_font_family</item>
        <item name="android:textColor">@color/text_color</item>
    </style>

    <style name="HintText" parent="TextAppearance.AppCompat">
        <item name="android:fontFamily">@font/custom_font_family</item>
        <item name="android:textColor">@color/dark_gray</item>
    </style>
</resources>
```

## 다크모드 적용시
다크모드 적용을 위한 리소스파일을 추가하였다.
```xml
<resources xmlns:tools="http://schemas.android.com/tools">
    <!-- Base application theme. -->
    <style name="Base.Theme.TeamSns" parent="Theme.Material3.DayNight.NoActionBar">
        <!-- Customize your dark theme here. -->
        <!-- <item name="colorPrimary">@color/my_dark_primary</item> -->
        <item name="android:textColor">@color/text_color</item>
        <item name="android:buttonStyle">@style/customButtonFontStyle</item>
        <item name="android:textViewStyle">@style/customTextViewFontStyle</item>
        <item name="android:editTextStyle">@style/customEditTextFontStyle</item>
    </style>
    <style name="PopupTheme" parent="Theme.AppCompat.Light.Dialog">
        <item name="android:windowIsTranslucent">true</item>
        <item name="android:windowBackground">@android:color/transparent</item>
        <item name="android:windowContentOverlay">@null</item>
        <item name="android:windowDrawsSystemBarBackgrounds">true</item>
        <item name="android:windowIsFloating">true</item>
        <item name="android:backgroundDimEnabled">true</item>
    </style>

</resources>
```

## Color
다크모드적용시와 평상시의 구분을 위해 color.xml파일로 분류해두고 있다.
### 평상시
```xml
<resources>
    <color name="black">#FF000000</color>
    <color name="white">#FFFFFFFF</color>
    <color name="background_color">#FADAD8</color>
    <color name="component_color">#FFFFFFFF</color>
    <color name="text_color">#FF1B212D</color>

    <color name="red">#FF0000</color>
    <color name="orange">#FFA500</color>
    <color name="yellow">#FFFF00</color>
    <color name="green">#008000</color>
    <color name="blue">#0000FF</color>
    <color name="indigo">#4B0082</color>
    <color name="violet">#EE82EE</color>

    <color name="light_green">#90EE90</color>
    <color name="sky_blue">#87CEEB</color>
    <color name="light_blue">#ADD8E6</color>
    <color name="purple">#800080</color>
    <color name="pink">#FFC0CB</color>
    <color name="brown">#A52A2A</color>
    <color name="gray">#808080</color>
    <color name="light_gray">#D3D3D3</color>
    <color name="dark_gray">#A9A9A9</color>
</resources>
```
### 다크모드 생상
```xml
<resources>
    <color name="black">#FFFFFFFF</color>
    <color name="white">#FF000000</color>
    <color name="background_color">#3A3F4C</color>
    <color name="component_color">#525A60</color>
    <color name="text_color">#fd999a</color>

    <color name="red">#008000</color>
    <color name="orange">#0000FF</color>
    <color name="yellow">#4B0082</color>
    <color name="green">#FF0000</color>
    <color name="blue">#FFA500</color>
    <color name="indigo">#FFFF00</color>
    <color name="violet">#EE82EE</color>

    <color name="light_green">#800080</color>
    <color name="sky_blue">#FFC0CB</color>
    <color name="light_blue">#A52A2A</color>
    <color name="purple">#90EE90</color>
    <color name="pink">#87CEEB</color>
    <color name="brown">#ADD8E6</color>
    <color name="gray">#D3D3D3</color>
    <color name="light_gray">#A9A9A9</color>
    <color name="dark_gray">#808080</color>
</resources>
```

