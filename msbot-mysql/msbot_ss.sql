-- 烧烧 额外的表

create table ms_level_exp
(
    lv        int    not null
        primary key,
    total_exp bigint null,
    need_exp  bigint null
);

create table official_news
(
    id        int auto_increment
        primary key,
    title     varchar(125)                       null,
    img_path  varchar(255)                       null,
    url       varchar(1023)                      null,
    create_ts datetime default CURRENT_TIMESTAMP null
);

create table ms.official_news_listener
(
    id       int auto_increment
        primary key,
    qq       bigint  null,
    type     tinyint null,
    in_valid tinyint null
);

create table flag_listener(qq bigint primary key , in_valid tinyint, msg text, creator bigint);

insert into ms_level_exp (lv, total_exp, need_exp)
values  (200, 11462335230, 2207026470),
        (201, 13669361700, 2471869646),
        (202, 16141231346, 2768494003),
        (203, 18909725349, 3100713283),
        (204, 22010438632, 3472798876),
        (205, 25483237508, 3889534741),
        (206, 29372772249, 4356278909),
        (207, 33729051158, 4879032378),
        (208, 38608083536, 5464516263),
        (209, 44072599799, 6120258214),
        (210, 50192858013, 9792413142),
        (211, 59985271155, 10869578587),
        (212, 70854849742, 12065232231),
        (213, 82920081973, 13392407776),
        (214, 96312489749, 14865572631),
        (215, 111178062380, 19325244420),
        (216, 130503306800, 21064516417),
        (217, 151567823217, 22960322894),
        (218, 174528146111, 25026751954),
        (219, 199554898065, 27279159629),
        (220, 226834057694, 43646655406),
        (221, 270480713100, 46701921284),
        (222, 317182634384, 49971055773),
        (223, 367153690157, 53469029677),
        (224, 420622719834, 57211861754),
        (225, 477834581588, 74375420280),
        (226, 552210001868, 78094191294),
        (227, 630304193162, 81998900858),
        (228, 712303094020, 86098845900),
        (229, 798401939920, 90403788195),
        (230, 888805728115, 144646061112),
        (231, 1033451789227, 148985442945),
        (232, 1182437232172, 153455006233),
        (233, 1335892238405, 158058656419),
        (234, 1493950894824, 162800416111),
        (235, 1656751310935, 211640540944),
        (236, 1868391851879, 217989757172),
        (237, 2086381609051, 224529449887),
        (238, 2310911058938, 231265333383),
        (239, 2542176392321, 238203293384),
        (240, 2780379685705, 381125269414),
        (241, 3161504955119, 392559027496),
        (242, 3554063982615, 404335798320),
        (243, 3958399780935, 416465872269),
        (244, 4374865653204, 428959848437),
        (245, 4803825501641, 557647802968),
        (246, 5361473304609, 574377237057),
        (247, 5935850541666, 591608554168),
        (248, 6527459095834, 609356810793),
        (249, 7136815906627, 627637515116),
        (250, 7764453421743, 1004220024185),
        (251, 8768673445928, 1034346624910),
        (252, 9803020070838, 1065377023657),
        (253, 10868397094495, 1097338334366),
        (254, 11965735428861, 1130258484396),
        (255, 13095993913257, 1164166238927),
        (256, 14260160152184, 1199091226094),
        (257, 15459251378278, 1235063962876),
        (258, 16694315341154, 1272115881762),
        (259, 17966431222916, 1310279358214),
        (260, 19276710581130, 2902427248153),
        (261, 22179137829283, 2931451520634),
        (262, 25110589349917, 2960766035840),
        (263, 28071355385757, 2990373696198),
        (264, 31061729081955, 3020277433159),
        (265, 34082006515114, 3050480207490),
        (266, 37132486722604, 3080985009564),
        (267, 40213471732168, 3111794859659),
        (268, 43325266591827, 3142912808255),
        (269, 46468179400082, 3174341936337),
        (270, 49642521336419, 6412170711400),
        (271, 56054692047819, 6476292418514),
        (272, 62530984466333, 6541055342699),
        (273, 69072039809032, 6606465896125),
        (274, 75678505705157, 6672530555086),
        (275, 82351036260243, 13478511721273),
        (276, 95829547981516, 14826362893400),
        (277, 110655910874916, 16308999182740),
        (278, 126964910057656, 17939899101014),
        (279, 144904809158670, 19733889011115),
        (280, 164638698169785, 39862455802452),
        (281, 204501153972237, 43848701382697),
        (282, 248349855354934, 48233571520966),
        (283, 296583426875900, 53056928673062),
        (284, 349640355548962, 58362621540368),
        (285, 408002977089330, 117892495511543),
        (286, 525895472600873, 129681745062697),
        (287, 655577217663570, 142649919568966),
        (288, 798227137232536, 156914911525862),
        (289, 955142048758398, 172606402678448),
        (290, 1127748451436846, 348664933410464),
        (291, 1476413384847310, 383531426751510),
        (292, 1859944811598820, 421884569426661),
        (293, 2281829381025481, 464073026369327),
        (294, 2745902407394808, 510480329006259),
        (295, 3256382736401067, 1031170264592643),
        (296, 4287553000993710, 1134287291051907),
        (297, 5421840292045617, 1247716020157097),
        (298, 6669556312202714, 1372487622172806),
        (299, 8042043934375520, 2058731433259209),
        (300, 10100775367634729, 0);