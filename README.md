[![Download](https://api.bintray.com/packages/moreno/maven/lastadapter/images/download.svg)](https://bintray.com/moreno/maven/lastadapter/_latestVersion)
[![License](https://img.shields.io/:License-Apache-orange.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

# LastAdapter

LastAdapter is an Android library that avoids you the need of writing adapters and viewholders when working with RecyclerView. It is based on [Data Binding](https://developer.android.com/topic/libraries/data-binding/index.html) and written in [Kotlin](http://kotlinlang.org).

## Usage

Enable data binding in your project and create your item layouts in that way:

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android" >

    <data>
        <variable name="item" type="com.github.nitrico.lastadapterproject.item.Header" />
    </data>
    
    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@{item.text}" />
        
</layout>
```

**It is important that all the item types have the same variable name**, in this case "item". 
This name is passed to the adapter builder as BR.variableName, in this case BR.item:

#### Java

```java
LastAdapter.with(listOfItems, BR.item)
           .map(Header.class, R.layout.item_header)
           .map(Point.class, R.layout.item_point)
           .into(recyclerView);
```

#### Kotlin

```kotlin
LastAdapter.with(listOfItems, BR.item)
           .map<Header>(R.layout.item_header)  // or .map(Header::class.java, R.layout.item_header)
           .map<Point>(R.layout.item_point)    // or .map(Point::class.java, R.layout.item_point)
           .into(recyclerView)
```

---

The list of items can be an `ObservableList` if you want to get the adapter **automatically updated** when its content changes, or a simple `List` if you don't need to use this feature.

Use `.build()` method instead of `.into(recyclerView)` if you want to create the adapter but don't assign it to the RecyclerView yet. Both methods return the adapter.

If there is any operation that you can't achieve through Data Binding, you can set an **OnBindListener** with `.onBindListener(listener)` before calling .build or .into()

## Setup

#### Gradle

```gradle
android {
    ...
    dataBinding { 
        enabled true 
    }
}

dependencies {
    compile 'com.github.nitrico.lastadapter:lastadapter:0.1.2'
}
```

## Acknowledgments

Thanks to **Yigit Boyar** and **George Mount** for [this talk](https://realm.io/news/data-binding-android-boyar-mount/).

## License
```txt
Copyright 2016 Miguel Ángel Moreno

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
