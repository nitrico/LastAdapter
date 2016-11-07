[![Download](https://api.bintray.com/packages/moreno/maven/lastadapter/images/download.svg)](https://bintray.com/moreno/maven/lastadapter/_latestVersion)
[![Size](https://img.shields.io/badge/Size-31 KB-e91e63.svg)](http://www.methodscount.com/?lib=com.github.nitrico.lastadapter%3Alastadapter%3A%2B)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-LastAdapter-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/3810)
[![License](https://img.shields.io/:License-Apache 2.0-orange.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Gitter](https://badges.gitter.im/nitrico/LastAdapter.svg)](https://gitter.im/nitrico/LastAdapter?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

# LastAdapter

**Don't write a RecyclerView adapter again. Not even a ViewHolder!**

* Based on [**Android Data Binding**](https://developer.android.com/topic/libraries/data-binding/index.html)
* Written in [**Kotlin**](http://kotlinlang.org)
* No need to write the adapter
* No need to write the viewholders
* No need to modify your model classes
* No need to notify the adapter when data set changed
* Supports multiple view types
* Manage item click/long-click in layout or builder
* Optional OnBindListener
* Very fast — no reflection
* Super easy API
* Tiny size: **31 KB**
* Minimum Android SDK: **9**


## Setup

### Gradle

```gradle
android {
    ...
    dataBinding { 
        enabled true 
    }
}

dependencies {
    compile 'com.github.nitrico.lastadapter:lastadapter:1.2.2'
    // kapt 'com.android.databinding:compiler:GRADLE_PLUGIN_VERSION'  // this line only for Kotlin projects
}

// kapt { generateStubs = true }  // this line only for Kotlin projects
```


## Usage

Create your item layouts with `<layout>` as root:

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

### Java

```java
LastAdapter.with(listOfItems, BR.item)
           .map(Header.class, R.layout.item_header)
           .map(Point.class, R.layout.item_point)
           .onBindListener(this)       // Optional. 'this' is LastAdapter.OnBindListener
           .onClickListener(this)      // Optional. 'this' is LastAdapter.OnClickListener
           .onLongClickListener(this)  // Optional. 'this' is LastAdapter.OnLongClickListener
           .into(recyclerView);
```

### Kotlin

```kotlin
LastAdapter.with(listOfItems, BR.item)
           .map<Header>(R.layout.item_header)  // or .map(Header::class.java, R.layout.item_header)
           .map<Point>(R.layout.item_point)    // or .map(Point::class.java, R.layout.item_point)
           // 'item: Any', 'view: View', 'type: Int' and 'position: Int' are available inside the lambdas
           .onBind { println("binded view $view at position $position: $item") }             // Optional
           .onClick { println("clicked view $view at position $position: $item") }           // Optional
           .onLongClick { println("long-clicked view $view at position $position: $item") }  // Optional
           .into(recyclerView)
```
---

The list of items can be an `ObservableList` if you want to get the adapter **automatically updated** when its content changes, or a simple `List` if you don't need to use this feature.

Use `.build()` method instead of `.into(recyclerView)` if you want to create the adapter but don't assign it to the RecyclerView yet. Both methods return the adapter.

`type` parameter in `onBind`, `onClick` and `onLongClick` is an integer value that **matches the layout resource id** used for each item type. You can use this value to:
* Manage clicks for different item types in different ways.
* Get the right binding class for each item (avoiding `findViewById` calls) if there is any operation that you cannot or simply don't want to do using Data Binding.

### LayoutHandler

The LayoutHandler interface allows you to use different layouts based on more complex criteria. Its one single method receives the item and the position and returns the layout resource id.

```java
// Java sample
LastAdapter.with(listOfItems, BR.item)
           .layoutHandler(typeHandler)
           .into(recyclerView);

private LastAdapter.LayoutHandler typeHandler = new LastAdapter.LayoutHandler() {
    @Override public int getItemLayout(@NotNull Object item, int position) {
        if (item instanceof Header) {
            return (position == 0) ? R.layout.item_header_first : R.layout.item_header;
        } else {
            return R.layout.item_point;
        }
    }
};
```
```kotlin
// Kotlin sample
LastAdapter.with(listOfItems, BR.item)
           .layout { 
                when (item) { // 'item: Any' and 'position: Int' are available inside the lambda
                    is Header -> if (position == 0) R.layout.item_header_first else R.layout.item_header
                    else -> R.layout.item_point 
                }
            }.into(recyclerView)
```

### Custom fonts

You might also want to try [**FontBinder**](https://github.com/nitrico/FontBinder) to easily use custom fonts in your XML layouts.


## Acknowledgments

Thanks to **Yigit Boyar** and **George Mount** for [this talk](https://realm.io/news/data-binding-android-boyar-mount/).


## Author

#### Miguel Ángel Moreno

I'm open to new job positions - Contact me!

|[AngelList](https://angel.co/miguelangelmoreno)|[Email](mailto:nitrico@gmail.com)|[Facebook](https://www.facebook.com/miguelangelmoreno)|[Google+](https://plus.google.com/+Miguel%C3%81ngelMorenoS) |[Linked.in](https://www.linkedin.com/in/morenomiguelangel)|[Twitter](https://twitter.com/nitrico/)
|---|---|---|---|---|---|


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
