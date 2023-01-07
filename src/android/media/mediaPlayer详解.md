## mediaPlayer

MediaPlayer class can be used to control playback of audio/video files and streams.

MediaPlayer is not thread-safe. Creation of and all access to player instances should be on the same thread. If registering [callbacks](https://developer.android.com/reference/android/media/MediaPlayer#Callbacks), the thread must have a Looper.

Topics covered here are:

1. [State Diagram](https://developer.android.com/reference/android/media/MediaPlayer#StateDiagram)
2. [Valid and Invalid States](https://developer.android.com/reference/android/media/MediaPlayer#Valid_and_Invalid_States)
3. [Permissions](https://developer.android.com/reference/android/media/MediaPlayer#Permissions)
4. [Register informational and error callbacks](https://developer.android.com/reference/android/media/MediaPlayer#Callbacks)





### State Diagram

Playback control of audio/video files and streams is managed as a state machine. The following diagram shows the life cycle and the states of a MediaPlayer object driven by the supported playback control operations. The ovals represent the states a MediaPlayer object may reside in. The arcs represent the playback control operations that drive the object state transition. There are two types of arcs. The arcs with a single arrow head represent synchronous method calls, while those with a double arrow head represent asynchronous method calls.

![MediaPlayer State diagram](.\mediaplayer_state_diagram.gif)

From this state diagram, one can see that a MediaPlayer object has the following states:

- When a MediaPlayer object is just created using

   

  ```
  new
  ```

   

  or after

   

  ```
  reset()
  ```

   

  is called, it is in the

   

  Idle

   

  state; and after

   

  ```
  release()
  ```

   

  is called, it is in the

   

  End

   

  state. Between these two states is the life cycle of the MediaPlayer object.

  - There is a subtle but important difference between a newly constructed MediaPlayer object and the MediaPlayer object after `reset()` is called. It is a programming error to invoke methods such as `getCurrentPosition()`, `getDuration()`, `getVideoHeight()`, `getVideoWidth()`, `setAudioAttributes(android.media.AudioAttributes)`, `setLooping(boolean)`, `setVolume(float, float)`, `pause()`, `start()`, `stop()`, `seekTo(long, int)`, `prepare()` or `prepareAsync()` in the *Idle* state for both cases. If any of these methods is called right after a MediaPlayer object is constructed, the user supplied callback method OnErrorListener.onError() won't be called by the internal player engine and the object state remains unchanged; but if these methods are called right after `reset()`, the user supplied callback method OnErrorListener.onError() will be invoked by the internal player engine and the object will be transfered to the *Error* state.
  - You must call `release()` once you have finished using an instance to release acquired resources, such as memory and codecs. Once you have called `release()`, you must no longer interact with the released instance.
  - MediaPlayer objects created using `new` is in the *Idle* state, while those created with one of the overloaded convenient `create` methods are *NOT* in the *Idle* state. In fact, the objects are in the *Prepared* state if the creation using `create` method is successful.

- In general, some playback control operation may fail due to various reasons, such as unsupported audio/video format, poorly interleaved audio/video, resolution too high, streaming timeout, and the like. Thus, error reporting and recovery is an important concern under these circumstances. Sometimes, due to programming errors, invoking a playback control operation in an invalid state may also occur. Under all these error conditions, the internal player engine invokes a user supplied OnErrorListener.onError() method if an OnErrorListener has been registered beforehand via

   

  ```
  setOnErrorListener(android.media.MediaPlayer.OnErrorListener)
  ```

  .

  - It is important to note that once an error occurs, the MediaPlayer object enters the *Error* state (except as noted above), even if an error listener has not been registered by the application.
  - In order to reuse a MediaPlayer object that is in the *Error* state and recover from the error, `reset()` can be called to restore the object to its *Idle* state.
  - It is good programming practice to have your application register a OnErrorListener to look out for error notifications from the internal player engine.
  - IllegalStateException is thrown to prevent programming errors such as calling `prepare()`, `prepareAsync()`, or one of the overloaded `setDataSource `methods in an invalid state.

- Calling

   

  ```
  setDataSource(java.io.FileDescriptor)
  ```

  , or

   

  ```
  setDataSource(java.lang.String)
  ```

  , or

   

  ```
  setDataSource(android.content.Context, android.net.Uri)
  ```

  , or

   

  ```
  setDataSource(java.io.FileDescriptor, long, long)
  ```

  , or

   

  ```
  setDataSource(android.media.MediaDataSource)
  ```

   

  transfers a MediaPlayer object in the

   

  Idle

   

  state to the

   

  Initialized

   

  state.

  - An IllegalStateException is thrown if setDataSource() is called in any other state.
  - It is good programming practice to always look out for `IllegalArgumentException` and `IOException` that may be thrown from the overloaded `setDataSource` methods.

- A MediaPlayer object must first enter the

   

  Prepared

   

  state before playback can be started.

  - There are two ways (synchronous vs. asynchronous) that the *Prepared* state can be reached: either a call to `prepare()` (synchronous) which transfers the object to the *Prepared* state once the method call returns, or a call to `prepareAsync()` (asynchronous) which first transfers the object to the *Preparing* state after the call returns (which occurs almost right away) while the internal player engine continues working on the rest of preparation work until the preparation work completes. When the preparation completes or when `prepare()` call returns, the internal player engine then calls a user supplied callback method, onPrepared() of the OnPreparedListener interface, if an OnPreparedListener is registered beforehand via `setOnPreparedListener(android.media.MediaPlayer.OnPreparedListener)`.
  - It is important to note that the *Preparing* state is a transient state, and the behavior of calling any method with side effect while a MediaPlayer object is in the *Preparing* state is undefined.
  - An IllegalStateException is thrown if `prepare()` or `prepareAsync()` is called in any other state.
  - While in the *Prepared* state, properties such as audio/sound volume, screenOnWhilePlaying, looping can be adjusted by invoking the corresponding set methods.

- To start the playback,
  
  start()


  must be called. After

  start()


  returns successfully, the MediaPlayer object is in the

  Started

  state.

   

  ```
  isPlaying()
  ```

   

  can be called to test whether the MediaPlayer object is in the

   

  Started

   

  state.

  - While in the *Started* state, the internal player engine calls a user supplied OnBufferingUpdateListener.onBufferingUpdate() callback method if a OnBufferingUpdateListener has been registered beforehand via `setOnBufferingUpdateListener(android.media.MediaPlayer.OnBufferingUpdateListener)`. This callback allows applications to keep track of the buffering status while streaming audio/video.
  - Calling `start()` has no effect on a MediaPlayer object that is already in the *Started* state.

- Playback can be paused and stopped, and the current playback position can be adjusted. Playback can be paused via

   

  ```
  pause()
  ```

  . When the call to

   

  ```
  pause()
  ```

   

  returns, the MediaPlayer object enters the

   

  Paused

   

  state. Note that the transition from the

   

  Started

   

  state to the

   

  Paused

   

  state and vice versa happens asynchronously in the player engine. It may take some time before the state is updated in calls to

   

  ```
  isPlaying()
  ```

  , and it can be a number of seconds in the case of streamed content.

  - Calling `start()` to resume playback for a paused MediaPlayer object, and the resumed playback position is the same as where it was paused. When the call to `start()` returns, the paused MediaPlayer object goes back to the *Started* state.
  - Calling `pause()` has no effect on a MediaPlayer object that is already in the *Paused* state.

- Calling

   

  ```
  stop()
  ```

   

  stops playback and causes a MediaPlayer in the

   

  Started

  ,

   

  Paused

  ,

   

  Prepared 

  or

   

  PlaybackCompleted

   

  state to enter the

   

  Stopped

   

  state.

  - Once in the *Stopped* state, playback cannot be started until `prepare()` or `prepareAsync()` are called to set the MediaPlayer object to the *Prepared* state again.
  - Calling `stop()` has no effect on a MediaPlayer object that is already in the *Stopped* state.

- The playback position can be adjusted with a call to

   

  ```
  seekTo(long, int)
  ```

  .

  - Although the asynchronuous `seekTo(long, int)` call returns right away, the actual seek operation may take a while to finish, especially for audio/video being streamed. When the actual seek operation completes, the internal player engine calls a user supplied OnSeekComplete.onSeekComplete() if an OnSeekCompleteListener has been registered beforehand via `setOnSeekCompleteListener(android.media.MediaPlayer.OnSeekCompleteListener)`.
  - Please note that `seekTo(long, int)` can also be called in the other states, such as *Prepared*, *Paused* and *PlaybackCompleted* state. When `seekTo(long, int)` is called in those states, one video frame will be displayed if the stream has video and the requested position is valid.
  - Furthermore, the actual current playback position can be retrieved with a call to `getCurrentPosition()`, which is helpful for applications such as a Music player that need to keep track of the playback progress.

- When the playback reaches the end of stream, the playback completes.

  - If the looping mode was being set to true with `setLooping(boolean)`, the MediaPlayer object shall remain in the *Started* state.
  - If the looping mode was set to false , the player engine calls a user supplied callback method, OnCompletion.onCompletion(), if a OnCompletionListener is registered beforehand via `setOnCompletionListener(android.media.MediaPlayer.OnCompletionListener)`. The invoke of the callback signals that the object is now in the *PlaybackCompleted* state.
  - While in the *PlaybackCompleted* state, calling `start()` can restart the playback from the beginning of the audio/video source.