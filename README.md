[![Download](https://api.bintray.com/packages/moreno/maven/lastadapter/images/download.svg)](https://bintray.com/moreno/maven/lastadapter/_latestVersion)
[![License](https://img.shields.io/:License-Apache-orange.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

# LastAdapter

###### Don't write a RecyclerView adapter again. Not even a ViewHolder!

## Usage

#### Java

Create your layout using data binding. For example:

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android" >

    <data>
        <variable name="item" type="com.github.nitrico.lastadapterproject.item.Point" />
    </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="16dp"
        android:orientation="vertical"
        android:onClick="@{(v) -> item.onItemClick(v)}" >
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text='@{"" +item.x}' />
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text='@{"" +item.y}' />
    </LinearLayout>

</layout>
```


```java
LastAdapter.with(listOfItems, BR.item)
           .map(Header.class, R.layout.item_header)
           .map(Point.class, R.layout.item_point)
           .onBindListener(listener)  // optional                 
           .into(recyclerView);       // or build() if you don't want to assign it yet
```

#### Kotlin

```kotlin
LastAdapter.with(listOfItems, BR.item)
           .map<Header>(R.layout.item_header)  // or .map(Header::class.java, R.layout.item_header)
           .map<Point>(R.layout.item_point)    // or .map(Point::class.java, R.layout.item_point)
           .onBindListener(listener)           // optional
           .into(recyclerView)                 // or build() if you don't want to assign it yet
```

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
