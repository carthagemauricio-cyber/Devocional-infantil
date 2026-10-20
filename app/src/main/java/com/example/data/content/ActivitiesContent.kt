package com.example.data.content

import com.example.data.model.BadgeDef
import com.example.data.model.QuizQuestion

object ActivitiesContent {
    val quizQuestions: List<QuizQuestion> = listOf(
        QuizQuestion(
            id = "q_01",
            question = "Quantos dias demorou Deus para criar o mundo e descansar no sétimo dia?",
            options = listOf("3 dias", "6 dias de criação", "10 dias", "100 dias"),
            correctIndex = 1,
            explanation = "Deus criou tudo em seis dias com amor e descansou no sétimo dia, abençoando-o!"
        ),
        QuizQuestion(
            id = "q_02",
            question = "Que sinal colorido Deus colocou nas nuvens como promessa a Noé de que nunca mais destruiria a terra com dilúvio?",
            options = listOf("Uma estrela cadente", "Um raio de sol", "Um arco-íris", "Uma nuvem branca"),
            correctIndex = 2,
            explanation = "O arco-íris colorido nas nuvens é o sinal da aliança e da fidelidade de Deus para sempre!"
        ),
        QuizQuestion(
            id = "q_03",
            question = "O que usou o jovem pastor David para vencer o gigante Golias?",
            options = listOf("Uma espada de ferro", "Uma funda e pedrinhas com fé em Deus", "Um arco e flecha", "Um escudo pesado"),
            correctIndex = 1,
            explanation = "David confiou no poder do Deus vivo e usou apenas uma pedrinha e a sua funda de pastor!"
        ),
        QuizQuestion(
            id = "q_04",
            question = "Quem fechou a boca dos leões famintos para não fazerem mal a Daniel na cova?",
            options = listOf("Os guardas do rei", "Um anjo enviado por Deus", "O próprio rei Dario", "Um mágico"),
            correctIndex = 1,
            explanation = "Deus enviou o Seu santo anjo e fechou a boca dos leões porque Daniel era fiel e orava com amor."
        ),
        QuizQuestion(
            id = "q_05",
            question = "Onde nasceu o Menino Jesus quando Maria e José viajaram até Belém?",
            options = listOf("Num castelo de ouro", "Numa manjedoura num estábulo simples", "Num barco de pesca", "Numa tenda no deserto"),
            correctIndex = 1,
            explanation = "Jesus nasceu numa manjedoura humilde, mostrando que o amor e a simplicidade são muito preciosos."
        ),
        QuizQuestion(
            id = "q_06",
            question = "Quantos pãezinhos e peixinhos tinha o rapazinho que partilhou o seu almoço com Jesus?",
            options = listOf("10 pães e 5 peixes", "5 pães e 2 peixinhos", "1 pão e 1 peixe", "20 pães"),
            correctIndex = 1,
            explanation = "Com apenas cinco pãezinhos de cevada e dois peixinhos, Jesus deu graças e alimentou mais de 5.000 pessoas!"
        ),
        QuizQuestion(
            id = "q_07",
            question = "O que Jesus disse ao mar agitado e ao vento forte durante a grande tempestade?",
            options = listOf("'Vento, sopra mais forte!'", "'Silêncio, aquieta-te!'", "'Vamos remar depressa!'", "'Fujam do barco!'"),
            correctIndex = 1,
            explanation = "Jesus levantou-se com autoridade e disse 'Silêncio, aquieta-te!', e o mar ficou em grande bonança."
        ),
        QuizQuestion(
            id = "q_08",
            question = "O que fez o Bom Samaritano quando viu o homem ferido caído à beira do caminho?",
            options = listOf("Passou pelo outro lado", "Limpou as suas feridas com azeite, cuidou dele e levou-o para uma estalagem", "Riu-se dele", "Chamou os soldados"),
            correctIndex = 1,
            explanation = "O Bom Samaritano teve compaixão no coração, cuidou das feridas do homem e pagou a sua recuperação."
        ),
        QuizQuestion(
            id = "q_09",
            question = "O que fez o pai amoroso quando viu o filho pródigo a voltar para casa arrependido?",
            options = listOf("Fechou a porta na cara dele", "Correu ao seu encontro, abraçou-o e fez uma grande festa", "Mandou-o embora", "Castigou-o duramente"),
            correctIndex = 1,
            explanation = "O pai correu com os braços abertos, beijou o filho e fez uma festa de celebração pelo seu regresso!"
        ),
        QuizQuestion(
            id = "q_10",
            question = "Quantas vezes Pedro perguntou se devia perdoar, e o que Jesus respondeu?",
            options = listOf("Pedro disse 3 vezes, Jesus disse 5", "Pedro disse 7 vezes, Jesus disse até setenta vezes sete", "Jesus disse para nunca perdoar", "Pedro disse 10 vezes"),
            correctIndex = 1,
            explanation = "Jesus ensinou que devemos estar sempre prontos a perdoar de coração, sem ficar a contar as vezes!"
        ),
        QuizQuestion(
            id = "q_11",
            question = "Por que Zaqueu subiu a uma figueira brava (sicómoro)?",
            options = listOf("Para apanhar figos doces", "Porque era baixinho e queria muito ver Jesus passar", "Para fugir da chuva", "Para brincar às escondidas"),
            correctIndex = 1,
            explanation = "Zaqueu era de pequena estatura e não conseguia ver por cima da multidão, mas Jesus viu o seu coração e foi à sua casa!"
        ),
        QuizQuestion(
            id = "q_12",
            question = "Que notícia maravilhosa os anjos deram às mulheres no túmulo no domingo de manhã?",
            options = listOf("'Jesus ainda está a dormir'", "'Ele não está aqui; ressuscitou!'", "'Fechem o jardim'", "'Voltem para casa tristes'"),
            correctIndex = 1,
            explanation = "Os anjos anunciaram que Jesus ressuscitou e está vivo para sempre, trazendo esperança e salvação ao mundo!"
        )
    )

    val badges: List<BadgeDef> = listOf(
        BadgeDef("b_first_story", "Primeiro Passo", "Leste a tua primeira história bíblica!", "🌱", 1, "stories"),
        BadgeDef("b_five_stories", "Pequeno Leitor de Fé", "Completaste 5 histórias da Bíblia!", "📖", 5, "stories"),
        BadgeDef("b_ten_stories", "Explorador da Palavra", "Leste 10 histórias bíblicas incríveis!", "🧭", 10, "stories"),
        BadgeDef("b_twenty_stories", "Guardião da Bíblia", "Completaste 20 histórias do amor de Deus!", "🛡️", 20, "stories"),
        BadgeDef("b_first_devotional", "Coração em Oração", "Fizeste o teu primeiro devocional diário!", "🙏", 1, "devotionals"),
        BadgeDef("b_week_devotionals", "Semana Abençoada", "Completaste 7 dias de devocionais seguidos!", "⭐", 7, "devotionals"),
        BadgeDef("b_first_kindness", "Semente de Amor", "Cumpriste o teu primeiro Desafio de Bondade!", "❤️", 1, "kindness"),
        BadgeDef("b_five_kindness", "Mãos de Paz", "Realizaste 5 actos generosos de bondade!", "🤝", 5, "kindness"),
        BadgeDef("b_quiz_master", "Sábio da Bíblia", "Acertaste questões no Quiz Bíblico!", "🏆", 3, "quiz"),
        BadgeDef("b_full_plan", "Grande Vencedor da Fé", "Completaste os 30 dias do Plano Devocional!", "👑", 30, "devotionals")
    )

    val kindnessChallengesList: List<String> = listOf(
        "Dá um abraço quentinho nos teus pais e diz: 'Obrigado por cuidarem de mim'.",
        "Ajuda a arrumar a mesa do almoço ou jantar com alegria.",
        "Empresta um brinquedo ou lápis de cor a um amigo sem resmungar.",
        "Diz 'bom dia' com um grande sorriso à primeira pessoa que encontrares.",
        "Rega as plantas de casa ou do quintal com carinho.",
        "Faz um desenho alegre para alguém que esteja triste ou doente.",
        "Se tiveres algum desentendimento com alguém, sê o primeiro a pedir desculpa ou a perdoar.",
        "Arruma os teus sapatos e roupas sem ninguém precisar de mandar.",
        "Diz três coisas boas e sinceras sobre o teu irmão ou melhor amigo.",
        "Ora baixinho por alguém que esteja a passar por um momento difícil.",
        "Cede a tua vez numa brincadeira a alguém mais tímido.",
        "Partilha um pedaço do teu lanche saboroso com quem não trouxe comida.",
        "Agradece com entusiasmo à professora ou educadora pelo que te ensina.",
        "Apanha papéis do chão para manter a tua escola ou casa limpa.",
        "Liga ou envia um recado de amor para os teus avós ou tios."
    )
}
