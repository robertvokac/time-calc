package rvc.timecalc;

import java.awt.Color;
import java.awt.Font;

/**
 * @author Robert
 * @since 09.02.2024
 */
public class Vtipy {
    /**
     * https://bestvtip.cz/
     * https://www.vtipbaze.cz/
     */
    private static final String[] array = new String[] {
            "\"Když dosáhnete mého věku, 65 let, stanou se tři věci,\" vypráví důchodce Karel. \"Nejprve vás začne opouštět paměť a na ty dvě další si teď nemůžu vzpomenout.\"",
            "Žán, odjíždíme zrovna někam? \"Ne, pane.\" \"Tak to mi právě ukradli auto.\"",
            "Jestli se plaváním hubne, tak co dělají velryby špatně?",
            "\"Tak co, Pepíčku, kde jsi byl o prázdninách?\"\n"
            + "\n"
            + "\"S maminkou a jejím novým přítelem u moře.\"\n"
            + "\n"
            + "\"A jak ses měl s novým otcem?\"\n"
            + "\n"
            + "\"Výborně, vždycky ráno mě odvezl daleko od břehu a já jsem plaval zpátky.\"\n"
            + "\n"
            + "\"To jsi zvládnul?\"\n"
            + "\n"
            + "\"V pohodě, akorát mi dělalo problém dostat se z toho zavázanýho pytle!\"",
            "Malý Ferko se ve škole vytahuje, že maminka porodila sestřičku.\n"
            + "Učitel ho pokárá, že sestřičku koupil tatínek.\n"
            + "\"Ale pane učiteli\" usměje se Ferko, \"to neznáte našeho tatínka... Ten co může udělat sám zadarmo, přece nikdy nekupuje.\"",
            "Svůdník Bond\n"
            + "Muži a ženy\n"
            + "Sebejistý James Bond se vrátil do Anglie, na letišti vejde do baru a sedne si vedle velmi atraktivní ženy. Rychle si jí prohlédne a tradičně se podívá na hodinky na ruce.\n"
            + "\n"
            + "Žena to zpozoruje a zeptá se: \"Vaše přítelkyně má zpoždění?\"\n"
            + "\n"
            + "\"Ne\", odpovídá Bond, \"agent Q mi dal tyhle nové hodinky, tak jsem je jen testoval.\"\n"
            + "\n"
            + "Ženu to zaujalo a ptá se: \"A co je na nich tak speciálního?\"\n"
            + "\n"
            + "Bond vysvětluje: \"Používají alfa vlny a telepaticky se mnou hovoří.\n"
            + "\n"
            + "Žena se zeptá: \"A copak vám říkají?\"\n"
            + "\n"
            + "\"Víte, říkají mi, že na sobě nemáte kalhotky.\"\n"
            + "\n"
            + "Žena se zachichotá a odpovídá: \"Pak musí být rozbité, protože já kalhotky mám!\"\n"
            + "\n"
            + "Bond se pousměje, poklepe na hodinky a povídá: \"Hmm, to je tím časovým posunem, jdou o hodinu napřed!\"",
            "Zvířátka jela na dovolenou a chovala se jako lidé:\n"
            + "Lišák jel s milenkou.\n"
            + "Osel jel s manželkou.\n"
            + "Vůl s celou rodinou.\n"
            + "Kanec jel sám a čekal, která svině se k němu přidá.",
            "Víte, který koncert stojí 45 centů?50 Cent feat. Nickelback.",
            "Jak zachránit člověka před udušením?\n"
            + "Někdy stačí tak málo, jako třeba sundat pytel z jejich hlavy.",
            "Leží guláš v žaludku a najednou bum, přiletí vodka, bum fernet, bum spláchne se to pivem. Gulášovi je to divný, protáhne se a řekne: Nahoře je asi ňákej mejdan, jdu se tam podívat.",
            "V MHD stojí chlap nad sedící ženskou:\n"
            + "\"Pane, kouká vám kousek přirození!\"\n"
            + "\"To není kousek, to je celý!\"",
            "Policista se ptá řidiče: 'Proč jste zastřelil tu krásnou cikánku?'\n"
            + "'Jsem pověrčivý a bál jsem se, že by mi, černá kočka, přeběhla přes cestu!'",
            "V pražské škole se pražská učitelka ptá pražských žáčků:\n"
            + "\n"
            + "\"Děti, kterému fotbalovému mužstvu fandíte?\"\n"
            + "\n"
            + "Zvedne se Anička: \"Spartě.\"\n"
            + "\n"
            + "Zvedně se Frantík: \"Spartě.\"\n"
            + "\n"
            + "Nakonec se zvedne Pepíček a povídá: \"Já fandím Baníku.\"\n"
            + "\n"
            + "Učitelka se díví a zeptá se, proč zrovna Baníku.\n"
            + "\n"
            + "Pepíček povídá: \" Z Ostravy pochází moje maminka.\n"
            + "\n"
            + "Do Ostravy se za ní přistěhoval tatínek, když se vzali, narodil jsem se tam a navíc moji rodiče oba fandí Baníku.\"\n"
            + "\n"
            + "Učitelka mírně napruzelým tónem povídá:\n"
            + "\n"
            + "\"Takže ty se opičíš po rodičích, co? To mi tedy řekni, co bys dělal, kdyby tvoje máma byla dementní a tvůj otec homosexuál?\"\n"
            + "\n"
            + "A Pepíček odpoví: \"Tak to bych nejspíš fandil Spartě.\"",
            "Jsou Windows XP vir?\n"
            + "\n"
            + "???\n"
            + "\n"
            + "Samozřejmě nejsou, nebo\u009D vir se šíří bezplatně, má úsporný kód a něco dělá.",
            "Víte, jaký je rozdíl mezi Windows a listím? . . . . . . . Listí padá jen na podzim.",
            "Jeden opilec si zkrátil cestu domů přes hřbitov. Tam však spadl do vykopané jámy a usnul. Ráno se prochladlý probudil a nadává: Sakra, to je kosa. A paní, co byla kousek dál položit kytičku, povídá: Tak co ty ses odkopal?",
            "Co ma spolecneho lahev a blondyna? - Od hrdla vyse jsou obe prazdne.",
            "Vždy, když dávám večer děti spát, přemýšlím, jestli je mám 'Uložit' anebo 'Uložit jako'..",
            "Kolik je na světě Somálců?\n"
            + "\n"
            + "65 kilo.",
            "Marie, oddaná katolička, se vdala a měla 10 dětí.\n"
            + "\n"
            + "Poté, co její první manžel zemřel, se znovu vdala a měla dalších 10 dětí.\n"
            + "\n"
            + "Několik týdnů potom, co ji její druhý manžel navždy opustil, zemřela i Marie.\n"
            + "\n"
            + "Při jejím pohřbu se kněz podíval k nebi a řekl: „Konečně, konečně jsou spolu.“\n"
            + "\n"
            + "Její sestra, která seděla v první řadě na to: “Promiňte, Otče, ale máte na mysli ji a jejího prvního, nebo ji a jejího druhého manžela?“\n"
            + "\n"
            + "Kněz odpověděl: „Myslím tím její nohy.“",
            "V parku stojí naproti sobě dvě sochy mladého muže a mladé ženy.\n"
            + "\n"
            + "Stojí rok, dva, tři...\n"
            + "\n"
            + "Přiletí za nima anděl a povídá:\n"
            + "\n"
            + "\"Když tady tak dlouho stojíte, dám vám pro radost půl hodiny lidskýho života. Můžete si dělat, co chcete.\"\n"
            + "\n"
            + "Sochy obživnou, mrknou na sebe a už upalujou do křoví. Po chvíli se ozývají různé zvuky, pochichtávání a za čtvrt hodiny celé šťastné vylezou.\n"
            + "\n"
            + "Anděl jim říká:\n"
            + "\n"
            + "\"Ještě máte čtvrt hodiny, můžete jít znovu.\"\n"
            + "\n"
            + "Tak sochy mažou a anděl najednou zaslechne:\n"
            + "\n"
            + "\"Ale teď to uděláme obráceně, ty budeš držet holuba a já se na něj vyseru.\""
    };

    public static void showRandom() {
        Toaster t = new Toaster();
        t.setToasterWidth(800);
        t.setToasterHeight(800);
        t.setDisplayTime(30000);
        t.setToasterColor(Color.GRAY);
        Font font = new Font("sans", Font.PLAIN, 16);
        t.setToasterMessageFont(font);
        t.showToaster(array[((int) (Math.random() * ((double) array.length)))]);
    }
}
