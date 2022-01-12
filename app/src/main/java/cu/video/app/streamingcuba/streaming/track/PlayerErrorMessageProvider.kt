package cu.video.app.streamingcuba.streaming.track

import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil
import com.google.android.exoplayer2.util.ErrorMessageProvider

class PlayerErrorMessageProvider :
    ErrorMessageProvider<ExoPlaybackException> {
    override fun getErrorMessage(e: ExoPlaybackException): android.util.Pair<Int, String> {
        var errorString = "Generic error ${e.message}"
        if (e.type == ExoPlaybackException.TYPE_RENDERER) {
            val cause = e.rendererException
            if (cause is MediaCodecRenderer.DecoderInitializationException) {
                // Special case for decoder initialization failures.
                errorString = if (cause.codecInfo?.name == null) {
                    when {
                        cause.cause is MediaCodecUtil.DecoderQueryException -> {
                            "Error query decoders"
                        }
                        cause.secureDecoderRequired -> {
                            "error_no_secure_decoder"
                        }
                        else -> {
                            "error_no_decoder"
                        }
                    }
                } else {
                    "Instanciando decoder"
                }
            }
        }
        return android.util.Pair(0, errorString)
    }
}