package com.badeling.msbot.infrastructure.maplegg.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RankResponse {
    @JsonProperty(value = "CharacterData")
    private CharacterData characterData;
}


/**
 * {
 * "CharacterData": {
 * "AchievementPoints": 7190,
 * "AchievementRank": 21495,
 * "CharacterImageURL": "https://msavatar1.nexon.net/Character/JHPEOJGELGLNJEELJOLJOADJHGNAHEILLKNNMPOGFHHPLJEDEBOFLKBOGKNNGOPIAOHEDFGGLMLDPAGGHBNHMPOOHKHPEEJEJOMGPPBHPFIOLJBPHIFPKBMJALEMOMLMMBGAMBPBMEJIMFJHDHDMKBHHGIMMAONEKJMJOBOAHEPPDIKLDKKLLMIJHJOOAJLDADDJNKIAIFKOOMPOIOJIHPFHHOJDHIMNKKBOKCAMLALBFKKEAPCADKGKDCDEFMHI.png",
 * "Class": "Night Lord",
 * "ClassRank": 1691,
 * "EXP": 1142813874012,
 * "EXPPercent": 81.14,
 * "GlobalRanking": 21318,
 * "GraphData": [
 * {
 * "AvatarURL": "https://msavatar1.nexon.net/Character/JHPEOJGELGLNJEELJOLJOADJHGNAHEILLKNNMPOGFHHPLJEDEBOFLKBOGKNNGOPIAOHEDFGGLMLDPAGGHBNHMPOOHKHPEEJEJOMGPPBHPFIOLJBPHIFPKBMJALEMOMLMMBGAMBPBMEJIMFJHDHDMKBHHGIMMAONEKJMJOBOAHEPPDIKLDKKLLMIJHJOOAJLDADDJNKIAIFKOOMPOIOJIHPFHHOJDHIMNKKBOKCAMLALBFKKEAPCADKGKDCDEFMHI.png",
 * "ClassID": 412,
 * "ClassRankGroupID": 400,
 * "CurrentEXP": 1142813874012,
 * "DateLabel": "2023-01-25",
 * "EXPDifference": 0,
 * "EXPToNextLevel": 265719772056,
 * "ImportTime": 1674712800,
 * "Level": 257,
 * "Name": "WaaCaa",
 * "ServerID": 45,
 * "ServerMergeID": 0,
 * "TotalOverallEXP": 18384155667562
 * },
 * {
 * "AvatarURL": "https://msavatar1.nexon.net/Character/JHPEOJGELGLNJEELJOLJOADJHGNAHEILLKNNMPOGFHHPLJEDEBOFLKBOGKNNGOPIAOHEDFGGLMLDPAGGHBNHMPOOHKHPEEJEJOMGPPBHPFIOLJBPHIFPKBMJALEMOMLMMBGAMBPBMEJIMFJHDHDMKBHHGIMMAONEKJMJOBOAHEPPDIKLDKKLLMIJHJOOAJLDADDJNKIAIFKOOMPOIOJIHPFHHOJDHIMNKKBOKCAMLALBFKKEAPCADKGKDCDEFMHI.png",
 * "ClassID": 412,
 * "ClassRankGroupID": 400,
 * "CurrentEXP": 1142813874012,
 * "DateLabel": "2023-01-26",
 * "EXPDifference": 0,
 * "EXPToNextLevel": 265719772056,
 * "ImportTime": 1674799200,
 * "Level": 257,
 * "Name": "WaaCaa",
 * "ServerID": 45,
 * "ServerMergeID": 0,
 * "TotalOverallEXP": 18384155667562
 * }
 * ],
 * "Guild": "",
 * "LegionCoinsPerDay": 154,
 * "LegionLevel": 5424,
 * "LegionPower": 178465211,
 * "LegionRank": 25868,
 * "Level": 257,
 * "Name": "WaaCaa",
 * "Server": "Reboot (NA)",
 * "ServerClassRanking": 1074,
 * "ServerRank": 15075,
 * "ServerSlug": "reboot-(na)"
 * }
 * }
 */
