[![Download](https://api.bintray.com/packages/moreno/maven/lastadapter/images/download.svg)](https://bintray.com/moreno/maven/lastadapter/_latestVersion)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-LastAdapter-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/3810)
[![License](https://img.shields.io/badge/License-Apache%202.0-orange.svg)](https://opensource.org/licenses/Apache-2.0)
[![Gitter](https://badges.gitter.im/nitrico/LastAdapter.svg)](https://gitter.im/nitrico/LastAdapter?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

# LastAdapter

**Don't write a RecyclerView adapter again. Not even a ViewHolder!**

* Based on [**Android Data Binding**](https://developer.android.com/topic/libraries/data-binding/index.html)
* Written in [**Kotlin**](http://kotlinlang.org)
* No need to write the adapter
* No need to write the viewholders
* No need to modify your model classes
* No need to notify the adapter when data set changes
* Supports multiple item view types
* Optional Callbacks/Listeners
* Very fast — no reflection
* Super easy API
* Tiny size: **26 KB**
* Minimum Android SDK: **9**


## Setup

### Gradle

```gradle
// apply plugin: 'kotlin-kapt' // this line only for Kotlin projects

android {
    ...
    dataBinding.enabled true 
}

dependencies {
    compile 'com.github.nitrico.lastadapter:lastadapter:2.2.2'
    // kapt 'com.android.databinding:compiler:GRADLE_PLUGIN_VERSION' // this line only for Kotlin projects
}
```


## Usage

Create your item layouts with `<layout>` as root:

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android">

    <data>
        <variable name="item" type="com.github.nitrico.lastadapterproject.item.Header"/>
    </data>
    
    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@{item.text}"/>
        
</layout>
```

**It is important for all the item types to have the same variable name**, in this case "item". 
This name is passed to the adapter builder as BR.variableName, in this case BR.item:

```java
// Java
new LastAdapter(listOfItems, BR.item)
           .map(Header.class, R.layout.item_header)
           .map(Point.class, R.layout.item_point)
           .into(recyclerView);
```
```kotlin     
// Kotlin
LastAdapter(listOfItems, BR.item)
           .map<Header>(R.layout.item_header)
           .map<Point>(R.layout.item_point)
           .into(recyclerView)
```

The list of items can be an `ObservableList` if you want to get the adapter **automatically updated** when its content changes, or a simple `List` if you don't need to use this feature.


### LayoutHandler

The LayoutHandler interface allows you to use different layouts based on more complex criteria. Its one single method receives the item and the position and returns the layout resource id.

```java
// Java sample
new LastAdapter(listOfItems, BR.item)
           .handler(typeHandler)
           .into(recyclerView);

private LayoutHandler typeHandler = new LayoutHandler() {
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
LastAdapter(listOfItems, BR.item).layout { item, position ->
    when (item) {
        is Header -> if (position == 0) R.layout.item_header_first else R.layout.item_header
        else -> R.layout.item_point 
    }
}.into(recyclerView)
```

For further information, please take a look at [my article at Medium](https://medium.com/@miguelangelmoreno/dont-write-recyclerview-adapters-b1dbc2c683bb).

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
