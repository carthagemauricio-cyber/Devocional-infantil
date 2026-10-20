package com.example.data.ai

import com.example.BuildConfig
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class PartData(
    @param:Json(name = "text") @field:Json(name = "text") val text: String? = null
)

@JsonClass(generateAdapter = true)
data class ContentData(
    @param:Json(name = "parts") @field:Json(name = "parts") val parts: List<PartData>
)

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    @param:Json(name = "contents") @field:Json(name = "contents") val contents: List<ContentData>
)

@JsonClass(generateAdapter = true)
data class CandidateData(
    @param:Json(name = "content") @field:Json(name = "content") val content: ContentData? = null
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    @param:Json(name = "candidates") @field:Json(name = "candidates") val candidates: List<CandidateData>? = null
)

interface GeminiApi {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object GeminiPersonalizationService {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    private val okHttpClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()
    }

    private val api: GeminiApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiApi::class.java)
    }

    suspend fun generatePersonalizedDevotional(
        childName: String,
        childAge: Int,
        topicOrFeeling: String,
        isPrayer: Boolean
    ): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (_: Exception) {
            ""
        }

        val prompt = if (isPrayer) {
            """
            Escreve uma oração cristã infantil doce, poética e acolhedora em língua portuguesa (adequada para Moçambique),
            para uma criança chamada "$childName" que tem $childAge anos de idade.
            Situação ou sentimento que a criança quer partilhar com Deus hoje: "$topicOrFeeling".
            Requisitos:
            - Linguagem meiga, amorosa, respeitosa e clara, adequada para uma criança de $childAge anos.
            - Lembrar que Jesus a ama pessoalmente e que Deus cuida dela.
            - Incluir um versículo bíblico reconfortante no final.
            - Sem jargões complicados, sem ameaças ou medo. Foco em amor, paz, coragem e bondade.
            """.trimIndent()
        } else {
            """
            Escreve uma pequena história devocional bíblica personalizada em língua portuguesa (adequada para Moçambique),
            para a criança "$childName" ($childAge anos).
            Tema ou situação que ela está a viver: "$topicOrFeeling".
            Requisitos:
            - Título encantador com emoji.
            - Breve narrativa conectando uma verdade bíblica com o dia a dia da criança.
            - O que aprendemos com Jesus.
            - Uma pergunta carinhosa para conversar com os pais.
            - Uma pequena oração final.
            - Foco no amor de Deus, paciência, perdão e confiança.
            """.trimIndent()
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            // High-quality contextual fallback
            return@withContext generateFallback(childName, childAge, topicOrFeeling, isPrayer)
        }

        try {
            val request = GeminiRequest(
                contents = listOf(
                    ContentData(parts = listOf(PartData(text = prompt)))
                )
            )
            val response = api.generateContent(apiKey, request)
            val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            if (!text.isNullOrBlank()) {
                text.trim()
            } else {
                generateFallback(childName, childAge, topicOrFeeling, isPrayer)
            }
        } catch (e: Exception) {
            generateFallback(childName, childAge, topicOrFeeling, isPrayer)
        }
    }

    private fun generateFallback(
        childName: String,
        childAge: Int,
        topicOrFeeling: String,
        isPrayer: Boolean
    ): String {
        return if (isPrayer) {
            """
            🌟 Oração Especial de $childName com Jesus

            "Querido Deus e Amado Pai do Céu,
            Hoje venho conversar Contigo com todo o meu coração.
            Tu conheces os meus passos e sabes o que estou a sentir: $topicOrFeeling.
            
            Obrigado porque nunca me deixas sozinho.
            Quando sinto medo, Tu és o meu escudo e a minha paz.
            Quando fico triste, Tu enxugas as minhas lágrimas.
            Quando estou feliz, Tu ris comigo!
            
            Segura na minha mão forte hoje e abençoa a minha família.
            Em nome do Teu Filho amado, Jesus,
            Amém!"

            📖 Versículo para o coração de $childName:
            "Não temas, porque eu sou contigo; não te assombres, porque eu sou o teu Deus; eu te fortaleço, e te ajudo." — Isaías 41:10
            """.trimIndent()
        } else {
            """
            📖 Uma História de Amor para $childName

            Tema: $topicOrFeeling
            Idade: $childAge anos

            Era uma vez um dia especial na vida de $childName. Às vezes o mundo parece cheio de coisas novas, desafios e momentos em que precisamos de um abraço carinhoso.

            Jesus olhou para $childName com um sorriso iluminado e disse: "Meu pequeno amigo, lembras-te dos passarinhos no céu? Eles cantam todas as manhãs sem medo porque sabem que o Pai do Céu cuida de cada um deles. E tu és infinitamente mais precioso para Mim do que todas as avezinhas!"

            Quando $childName pensa em $topicOrFeeling, pode lembrar-se de que Deus colocou no seu coração uma sementinha de coragem, bondade e amor. Com Jesus por perto, cada dia é uma oportunidade linda de aprender e vencer.

            💬 Para Conversar em Família:
            - Como o amor de Jesus nos ajuda a lidar com $topicOrFeeling?
            - O que podemos fazer juntos hoje para espalhar alegria em casa?

            🙏 Oraçãozinha:
            "Senhor Jesus, obrigado porque cuidas de $childName com tanto amor. Enche a nossa casa com a Tua paz. Amém!"
            """.trimIndent()
        }
    }
}
