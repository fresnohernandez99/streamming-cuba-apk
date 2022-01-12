package cu.video.app.streamingcuba.ui.fragments.player

import cu.video.app.streamingcuba.ui.base.BaseViewModel
import cu.video.app.streamingcuba.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    val networkHelper: NetworkHelper
) : BaseViewModel() {

    fun isOnline() = networkHelper.isOnline()
}