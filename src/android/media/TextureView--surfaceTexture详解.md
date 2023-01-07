

## 1.TextureView (纹理视图)

A TextureView can be used to display a content stream. Such a content stream can for instance be a video or an OpenGL scene. The content stream can come from the application's process as well as a remote process.
TextureView can only be used in a hardware accelerated window. When rendered in software, TextureView will draw nothing.
Unlike SurfaceView, TextureView does not create a separate window but behaves as a regular View. This key difference allows a TextureView to be moved, transformed, animated, etc. For instance, you can make a TextureView semi-translucent by calling myView.setAlpha(0.5f).
Using a TextureView is simple: all you need to do is get its SurfaceTexture. The SurfaceTexture can then be used to render content. 

A TextureView's SurfaceTexture can be obtained either by invoking getSurfaceTexture() or by using a TextureView.SurfaceTextureListener. It is important to know that a SurfaceTexture is available only after the TextureView is attached to a window (and onAttachedToWindow() has been invoked.) It is therefore highly recommended you use a listener to be notified when the SurfaceTexture becomes available.
It is important to note that only one producer can use the TextureView. For instance, if you use a TextureView to display the camera preview, you cannot use lockCanvas() to draw onto the TextureView at the same time.

>TextureView 可用于显示内容流。如，视频或 OpenGL 场景。内容流可以来自应用程序的进程以及远程进程。
>TextureView 只能在硬件加速窗口中使用。在软件中渲染时，TextureView 不会绘制任何内容。
>与SurfaceView不同，TextureView 不会创建单独的窗口，而是作为常规视图运行。这个关键区别允许 TextureView 移动、变换、动画等。例如，您可以通过调用myView.setAlpha(0.5f)使 TextureView 半透明
>
>可以通过调用getSurfaceTexture()或使用 TextureView.SurfaceTextureListener 来获取 TextureView 的TextureView.SurfaceTextureListener 。重要的是要知道 SurfaceTexture 仅在 TextureView 附加到窗口后才可用（并且已调用onAttachedToWindow() 。因此，强烈建议您使用侦听器，以便在 SurfaceTexture 可用时收到通知。
>重要的是要注意只有一个生产者可以使用 TextureView。例如，如果您使用 TextureView 显示相机预览，则不能同时使用lockCanvas()绘制到 TextureView 上

The following example demonstrates how to render the camera preview into a TextureView:

```java
    public class LiveCameraActivity extends Activity implements TextureView.SurfaceTextureListener {
        private Camera mCamera;
        private TextureView mTextureView;

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
      
            mTextureView = new TextureView(this);
            mTextureView.setSurfaceTextureListener(this);
      
            setContentView(mTextureView);
        }
      
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            mCamera = Camera.open();
      
            try {
                mCamera.setPreviewTexture(surface);
                mCamera.startPreview();
            } catch (IOException ioe) {
                // Something bad happened
            }
        }
      
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            // Ignored, Camera does all the work for us
        }
      
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            mCamera.stopPreview();
            mCamera.release();
            return true;
        }
      
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            // Invoked every time there's a new Camera preview frame
        }
    }
```

## 2.surfaceTexture(表面纹理)

Captures frames from an image stream as an OpenGL ES texture.

The image stream may come from either camera preview or video decode. A `Surface` created from a SurfaceTexture can be used as an output destination for the `android.hardware.camera2`, `MediaCodec`, `MediaPlayer`, and `Allocation` APIs. When `updateTexImage()` is called, the contents of the texture object specified when the SurfaceTexture was created are updated to contain the most recent image from the image stream. This may cause some frames of the stream to be skipped.

A SurfaceTexture may also be used in place of a SurfaceHolder when specifying the output destination of the older `Camera` API. Doing so will cause all the frames from the image stream to be sent to the SurfaceTexture object rather than to the device's display.

When sampling from the texture one should first transform the texture coordinates using the matrix queried via `getTransformMatrix(float[])`. The transform matrix may change each time `updateTexImage()` is called, so it should be re-queried each time the texture image is updated. This matrix transforms traditional 2D OpenGL ES texture coordinate column vectors of the form (s, t, 0, 1) where s and t are on the inclusive interval [0, 1] to the proper sampling location in the streamed texture. This transform compensates for any properties of the image stream source that cause it to appear different from a traditional OpenGL ES texture. For example, sampling from the bottom left corner of the image can be accomplished by transforming the column vector (0, 0, 0, 1) using the queried matrix, while sampling from the top right corner of the image can be done by transforming (1, 1, 0, 1).

The texture object uses the GL_TEXTURE_EXTERNAL_OES texture target, which is defined by the [GL_OES_EGL_image_external](http://www.khronos.org/registry/gles/extensions/OES/OES_EGL_image_external.txt) OpenGL ES extension. This limits how the texture may be used. Each time the texture is bound it must be bound to the GL_TEXTURE_EXTERNAL_OES target rather than the GL_TEXTURE_2D target. Additionally, any OpenGL ES 2.0 shader that samples from the texture must declare its use of this extension using, for example, an "#extension GL_OES_EGL_image_external : require" directive. Such shaders must also access the texture using the samplerExternalOES GLSL sampler type.

SurfaceTexture objects may be created on any thread. `updateTexImage()` may only be called on the thread with the OpenGL ES context that contains the texture object. The frame-available callback is called on an arbitrary thread, so unless special care is taken `updateTexImage()` should not be called directly from the callback.

>
>
>从图像流中捕获帧作为 OpenGL ES 纹理。 图像流可能来自相机预览或视频解码。 从 SurfaceTexture 创建的 Surface 可用作 android.hardware.camera2、MediaCodec、MediaPlayer 和 Allocation API 的输出目标。 调用 updateTexImage() 时，创建 SurfaceTexture 时指定的纹理对象的内容将更新为包含图像流中的最新图像。 这可能会导致流的某些帧被跳过。 在指定旧相机 API 的输出目标时，也可以使用 SurfaceTexture 代替 SurfaceHolder。 这样做会导致图像流中的所有帧都被发送到 SurfaceTexture 对象，而不是设备的显示器。 当从纹理中采样时，应该首先使用通过 getTransformMatrix(float[]) 查询的矩阵转换纹理坐标。 每次调用 updateTexImage() 时变换矩阵可能会发生变化，因此每次更新纹理图像时都应重新查询。 该矩阵将形式为 (s, t, 0, 1) 的传统 2D OpenGL ES 纹理坐标列向量（其中 s 和 t 在包含区间 [0, 1] 内）转换为流式纹理中的适当采样位置。 这种转换补偿了图像流源的任何属性，这些属性导致它看起来与传统的 OpenGL ES 纹理不同。 例如，从图像的左下角采样可以通过使用查询矩阵变换列向量 (0, 0, 0, 1) 来完成，而从图像的右上角采样可以通过变换 ( 1, 1, 0, 1). 纹理对象使用 GL_TEXTURE_EXTERNAL_OES 纹理目标，它由 GL_OES_EGL_image_external OpenGL ES 扩展定义。 这限制了纹理的使用方式。 每次绑定纹理时，它都必须绑定到 GL_TEXTURE_EXTERNAL_OES 目标而不是 GL_TEXTURE_2D 目标。 此外，任何从纹理采样的 OpenGL ES 2.0 着色器都必须使用例如“#extension GL_OES_EGL_image_external : require”指令来声明其对此扩展的使用。 此类着色器还必须使用 samplerExternalOES GLSL 采样器类型访问纹理。 SurfaceTexture 对象可以在任何线程上创建。 updateTexImage() 只能在具有包含纹理对象的 OpenGL ES 上下文的线程上调用。 帧可用回调在任意线程上调用，因此除非特别小心，否则不应直接从回调中调用 updateTexImage()。
