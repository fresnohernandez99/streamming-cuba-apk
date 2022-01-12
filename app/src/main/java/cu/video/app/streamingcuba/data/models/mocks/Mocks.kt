package cu.video.app.streamingcuba.data.models.mocks

import cu.video.app.streamingcuba.R
import cu.video.app.streamingcuba.data.models.entities.Event
import cu.video.app.streamingcuba.data.models.entities.Streaming
import cu.video.app.streamingcuba.data.models.entities.Streaming.Companion.TYPE_FREE
import cu.video.app.streamingcuba.data.models.entities.Streaming.Companion.TYPE_PREMIUM

object Mocks {

    var streamingFree = Streaming()
    var streamingPremium = Streaming()

    var event1 = Event()
    var event2 = Event()
    var event3 = Event()
    var event4 = Event()

    var arrayImg = arrayListOf(
        R.drawable.img_mock_3,
        R.drawable.img_mock_4,
        R.drawable.img_mock_1,
        R.drawable.img_mock_2
    )

    init {
        //Streamings
        streamingFree.id = 0
        streamingFree.title = "El título"
        streamingFree.imgUrl =
            "http://www.etecsa.cu/data/promociones/recarga_internacional_bonifica_tu_recarga_con_1250_cup_b1.jpg"
        streamingFree.description =
            "Súmate a esta transmisión para disfrutar de un espectáculo único y compartir desde cualquier lugar. (PLAYER)"
        streamingFree.url =
            "https://stmv2.zcastbr.com/streamingcuba/streamingcuba/playlist.m3u8"
        streamingPremium.imgRes = "img_mock_1"
        streamingFree.type = TYPE_FREE

        streamingPremium.id = 1
        streamingPremium.title = "El título"
        streamingPremium.imgUrl =
            "https://www.etecsa.cu/data/promociones/tarea_ordenamiento_2020_b1.jpg"
        streamingPremium.imgRes = "img_mock_2"
        streamingPremium.description =
            "Disfruta en GRANDE con el acceso PREMIUM de nuestra transmisión. (WEB PLAYER)"
        streamingPremium.url =
            "https://playervideo.zcastbr.com/video/streamingcuba/1/false/false/V1hwT1UyUkhVbkZUV0Zac1lsVTFiMWw2VGxOaFYwNXdUbGR3YVUxcVFUaz0rUg==/16:9//nao"
        streamingPremium.type = TYPE_PREMIUM


        //Events
        event1.id = 0
        event1.title = "Concierto de Raúl Paz en vivo"
        event1.date = "18/6/2021 3:45 PM"
        event1.imgUrl =
            "https://www.etecsa.cu/data/promociones/tarea_ordenamiento_2020_b1.jpg"
        event1.description =
            "Disfruta de nuestra transmisión."

        event2.id = 1
        event2.title = "Discoteca Virtual"
        event2.date = "20/6/2021 10:45 PM"
        event2.imgUrl =
            "https://www.etecsa.cu/data/promociones/tarea_ordenamiento_2020_b1.jpg"
        event2.description =
            "Disfruta de nuestra transmisión."

        event3.id = 2
        event3.title = "Bachata Electrónica Nocturna"
        event3.date = "22/6/2021 8:45 PM"
        event3.imgUrl =
            "https://www.etecsa.cu/data/promociones/tarea_ordenamiento_2020_b1.jpg"
        event3.description =
            "Disfruta de nuestra transmisión."

        event4.id = 3
        event4.title = "Entrevistas del Cubadisco"
        event4.date = "30/6/2021 3:45 PM"
        event4.imgUrl =
            "https://www.etecsa.cu/data/promociones/tarea_ordenamiento_2020_b1.jpg"
        event4.description =
            "Disfruta de nuestra transmisión."

    }

    var streamingsList = arrayListOf(
        streamingFree,
        streamingPremium
    )

    var eventList = arrayListOf(
        event1, event2, event3, event4
    )

}