package com.mrpowergamerbr.loritta.commands.vanilla.`fun`

import com.github.kevinsawicki.http.HttpRequest
import com.github.salomonbrys.kotson.array
import com.github.salomonbrys.kotson.get
import com.github.salomonbrys.kotson.string
import com.google.gson.JsonParser
import com.mrpowergamerbr.loritta.Loritta
import com.mrpowergamerbr.loritta.commands.CommandBase
import com.mrpowergamerbr.loritta.commands.CommandCategory
import com.mrpowergamerbr.loritta.commands.CommandContext
import com.mrpowergamerbr.loritta.utils.LorittaUtils

class RandomSAMCommand : CommandBase() {
	override fun getLabel(): String {
		return "randomsam"
	}

	override fun getAliases(): List<String> {
		return listOf("randomsouthamericamemes", "randommeme", "randommemes")
	}

	override fun getDescription(): String {
		return "Pega uma postagem aleatória do South America Memes"
	}

	override fun getCategory(): CommandCategory {
		return CommandCategory.FUN
	}

	override fun needsToUploadFiles(): Boolean {
		return true
	}

	override fun run(context: CommandContext) {
		val response = HttpRequest.get("https://graph.facebook.com/v2.9/samemes2/posts?fields=attachments{url,subattachments,media,description}&access_token=${Loritta.config.facebookToken}&offset=${Loritta.random.nextInt(0, 1000)}").body();

		val json = JsonParser().parse(response)

		var url: String? = null;
		var description: String? = null;

		for (post in json["data"].array) {
			var foundUrl = post["attachments"]["data"][0]["url"].string;

			if (!foundUrl.contains("video")) {
				url = post["attachments"]["data"][0]["media"]["image"]["src"].string;
				description = post["attachments"]["data"][0]["description"].string;
				break;
			}
		}

		if (url != null && description != null) {
			val image = LorittaUtils.downloadImage(url);
			context.sendFile(image, "south_america_memes.png", "<:sam:331592756969603073> | " + context.getAsMention(true) + "Cópia não é comédia! `$description`")
		} else {
			context.sendMessage(LorittaUtils.ERROR + " | " + context.getAsMention(true) + "Não consegui encontrar nenhum meme na página do South America Memes...")
		}
	}
}