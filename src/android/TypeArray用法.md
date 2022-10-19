

# 1.概念

```java
/**
 * Container for an array of values that were retrieved with
 * {@link Resources.Theme#obtainStyledAttributes(AttributeSet, int[], int, int)}
 * or {@link Resources#obtainAttributes}.  Be
 * sure to call {@link #recycle} when done with them.
 *
 * The indices used to retrieve values from this structure correspond to
 * the positions of the attributes given to obtainStyledAttributes.
 */
```

用于从布局文件里 检索对应的属性字段，主要用在自定义view中，自定义的一些字段

# 2.案列



```java
  public NineGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        // 把DP 转换为 PX
        gridSpacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, gridSpacing, dm);
        singleImageSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, singleImageSize, dm);
		
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NineGridView);
        gridSpacing = (int) a.getDimension(R.styleable.NineGridView_ngv_gridSpacing, gridSpacing);
        singleImageSize = a.getDimensionPixelSize(R.styleable.NineGridView_ngv_singleImageSize, singleImageSize);
        singleImageRatio = a.getFloat(R.styleable.NineGridView_ngv_singleImageRatio, singleImageRatio);
        maxImageSize = a.getInt(R.styleable.NineGridView_ngv_maxSize, maxImageSize);
        mode = a.getInt(R.styleable.NineGridView_ngv_mode, mode);
        a.recycle();

        imageViews = new ArrayList<>();
    }
```



## 1.TypedValue.applyDimension

TypedValue.applyDimension 用于把DP SP PT IN MM等单位转换为px

```java
  public static float applyDimension(int unit, float value,
                                       DisplayMetrics metrics)
    {
        switch (unit) {
        case COMPLEX_UNIT_PX:
            return value;
        case COMPLEX_UNIT_DIP:
            return value * metrics.density;
        case COMPLEX_UNIT_SP:
            return value * metrics.scaledDensity;
        case COMPLEX_UNIT_PT:
            return value * metrics.xdpi * (1.0f/72);
        case COMPLEX_UNIT_IN:
            return value * metrics.xdpi;
        case COMPLEX_UNIT_MM:
            return value * metrics.xdpi * (1.0f/25.4f);
        }
        return 0;
    }
```

## 2.obtainStyledAttributes

需要先定义要自定义的字段文件value/attrs.xml

```xml

<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="NineGridView">
        <attr name="ngv_singleImageSize" format="dimension"/>
        <attr name="ngv_singleImageRatio" format="float"/>
        <attr name="ngv_gridSpacing" format="dimension"/>
        <attr name="ngv_maxSize" format="integer"/>
        <attr name="ngv_mode" format="enum">
            <enum name="fill" value="0"/>
            <enum name="grid" value="1"/>
        </attr>
    </declare-styleable>
</resources>
```

declare-styleable name="NineGridView" 对应自定义view的name

 <attr name="ngv_singleImageSize" format="dimension"/>

属性name format 当前属性的类型

```xml
   <com.lll.ninegridview.NineGridView
        android:id="@+id/nine_grid"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_below="@+id/shortcoming"
        android:paddingTop="3dp"
        app:ngv_gridSpacing="3dp"
        android:paddingRight="3dp"
        android:paddingLeft="3dp"
        />
```

如ngv_gridSpacing 属性对应代码里gridSpacing = (int) a.getDimension(R.styleable.NineGridView_ngv_gridSpacing, gridSpacing)，获取字段的值

