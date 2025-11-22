// En repository/MockCardDataSource.kt
package com.example.carddotsearcher.repository

import com.example.carddotsearcher.model.Carta

object MockCardDataSource {

    // Una lista grande de cartas simuladas. Solo necesitamos el nombre para la lógica de inventario.
    // La 'type' y 'imageUrl' pueden estar vacías porque no se usan para esta parte.
    val mockCards = listOf(
        // Monstruos Clásicos
        Carta(name="Dark Magician", type="", imageUrl=""),
        Carta(name="Blue-Eyes White Dragon", type="", imageUrl=""),
        Carta(name="Red-Eyes Black Dragon", type="", imageUrl=""),
        Carta(name="Summoned Skull", type="", imageUrl=""),
        Carta(name="Gaia The Fierce Knight", type="", imageUrl=""),
        Carta(name="Celtic Guardian", type="", imageUrl=""),
        Carta(name="Kuriboh", type="", imageUrl=""),
        Carta(name="Exodia the Forbidden One", type="", imageUrl=""),
        Carta(name="Right Arm of the Forbidden One", type="", imageUrl=""),
        Carta(name="Left Arm of the Forbidden One", type="", imageUrl=""),
        Carta(name="Right Leg of the Forbidden One", type="", imageUrl=""),
        Carta(name="Left Leg of the Forbidden One", type="", imageUrl=""),
        Carta(name="Dark Magician Girl", type="", imageUrl=""),
        Carta(name="Buster Blader", type="", imageUrl=""),
        Carta(name="Jinzo", type="", imageUrl=""),

        // Monstruos de Fusión
        Carta(name="Blue-Eyes Ultimate Dragon", type="", imageUrl=""),
        Carta(name="Dark Paladin", type="", imageUrl=""),
        Carta(name="Gaia the Dragon Champion", type="", imageUrl=""),

        // Monstruos de Efecto Populares
        Carta(name="Sangan", type="", imageUrl=""),
        Carta(name="Witch of the Black Forest", type="", imageUrl=""),
        Carta(name="Man-Eater Bug", type="", imageUrl=""),
        Carta(name="Cyber Jar", type="", imageUrl=""),
        Carta(name="Morphing Jar", type="", imageUrl=""),
        Carta(name="Ash Blossom & Joyous Spring", type="", imageUrl=""),
        Carta(name="Effect Veiler", type="", imageUrl=""),
        Carta(name="Maxx \"C\"", type="", imageUrl=""),

        // Magias Icónicas
        Carta(name="Pot of Greed", type="", imageUrl=""),
        Carta(name="Monster Reborn", type="", imageUrl=""),
        Carta(name="Raigeki", type="", imageUrl=""),
        Carta(name="Harpie's Feather Duster", type="", imageUrl=""),
        Carta(name="Dark Hole", type="", imageUrl=""),
        Carta(name="Change of Heart", type="", imageUrl=""),
        Carta(name="Mystical Space Typhoon", type="", imageUrl=""),
        Carta(name="Swords of Revealing Light", type="", imageUrl=""),
        Carta(name="Polymerization", type="", imageUrl=""),
        Carta(name="Graceful Charity", type="", imageUrl=""),
        Carta(name="Book of Moon", type="", imageUrl=""),

        // Trampas Icónicas
        Carta(name="Mirror Force", type="", imageUrl=""),
        Carta(name="Magic Cylinder", type="", imageUrl=""),
        Carta(name="Trap Hole", type="", imageUrl=""),
        Carta(name="Solemn Judgment", type="", imageUrl=""),
        Carta(name="Call of the Haunted", type="", imageUrl=""),
        Carta(name="Torrential Tribute", type="", imageUrl=""),
        Carta(name="Ring of Destruction", type="", imageUrl=""),
        Carta(name="Imperial Order", type="", imageUrl=""),
        Carta(name="Bottomless Trap Hole", type="", imageUrl=""),

        // Dioses Egipcios
        Carta(name="Slifer the Sky Dragon", type="", imageUrl=""),
        Carta(name="Obelisk the Tormentor", type="", imageUrl=""),
        Carta(name="The Winged Dragon of Ra", type="", imageUrl=""),

        // HÉROES Elementales
        Carta(name="Elemental HERO Neos", type="", imageUrl=""),
        Carta(name="Elemental HERO Flame Wingman", type="", imageUrl=""),

                // Extra
        Carta(name="Time Wizard", type="", imageUrl=""),
        Carta(name="Scapegoat", type="", imageUrl=""),
        // Pega estas cartas al final de tu lista 'mockCards' existente.

// --- Monstruos de Sincronía ---
        Carta(name="Stardust Dragon", type="", imageUrl=""),
        Carta(name="Junk Warrior", type="", imageUrl=""),
        Carta(name="Black Rose Dragon", type="", imageUrl=""),
        Carta(name="Red Dragon Archfiend", type="", imageUrl=""),
        Carta(name="Trishula, Dragon of the Ice Barrier", type="", imageUrl=""),
        Carta(name="Brionac, Dragon of the Ice Barrier", type="", imageUrl=""),
        Carta(name="Crystal Wing Synchro Dragon", type="", imageUrl=""),
        Carta(name="PSY-Framelord Omega", type="", imageUrl=""),
        Carta(name="Scarlight Red Dragon Archfiend", type="", imageUrl=""),
        Carta(name="T.G. Hyper Librarian", type="", imageUrl=""),
        Carta(name="Baronne de Fleur", type="", imageUrl=""),
        Carta(name="Cosmic Blazar Dragon", type="", imageUrl=""),
        Carta(name="Shooting Quasar Dragon", type="", imageUrl=""),
        Carta(name="Swordsoul Grandmaster - Chixiao", type="", imageUrl=""),
        Carta(name="White Aura Whale", type="", imageUrl=""),

// --- Monstruos Xyz ---
        Carta(name="Number 39: Utopia", type="", imageUrl=""),
        Carta(name="Gagaga Cowboy", type="", imageUrl=""),
        Carta(name="Castel, the Skyblaster Musketeer", type="", imageUrl=""),
        Carta(name="Number 101: Silent Honor ARK", type="", imageUrl=""),
        Carta(name="Abyss Dweller", type="", imageUrl=""),
        Carta(name="Divine Arsenal AA-ZEUS - Sky Thunder", type="", imageUrl=""),
        Carta(name="Number 11: Big Eye", type="", imageUrl=""),
        Carta(name="Evilswarm Exciton Knight", type="", imageUrl=""),
        Carta(name="Bahamut Shark", type="", imageUrl=""),
        Carta(name="Toadally Awesome", type="", imageUrl=""),
        Carta(name="Raidraptor - Force Strix", type="", imageUrl=""),
        Carta(name="Number 60: Dugares the Timeless", type="", imageUrl=""),
        Carta(name="Time Thief Redoer", type="", imageUrl=""),
        Carta(name="Galaxy-Eyes Photon Dragon", type="", imageUrl=""),
        Carta(name="Number 92: Heart-eartH Dragon", type="", imageUrl=""),

// --- Monstruos de Péndulo ---
        Carta(name="Odd-Eyes Pendulum Dragon", type="", imageUrl=""),
        Carta(name="Astrograph Sorcerer", type="", imageUrl=""),
        Carta(name="Chronograph Sorcerer", type="", imageUrl=""),
        Carta(name="Performapal Skullcrobat Joker", type="", imageUrl=""),
        Carta(name="Wisdom-Eye Magician", type="", imageUrl=""),
        Carta(name="Heavymetalfoes Electrumite", type="", imageUrl=""),
        Carta(name="Qliphort Scout", type="", imageUrl=""),
        Carta(name="Guiding Ariadne", type="", imageUrl=""),
        Carta(name="Harmonizing Magician", type="", imageUrl=""),
        Carta(name="Supreme King Z-ARC", type="", imageUrl=""),

// --- Monstruos de Enlace (Link) ---
        Carta(name="Decode Talker", type="", imageUrl=""),
        Carta(name="Firewall Dragon", type="", imageUrl=""),
        Carta(name="Knightmare Phoenix", type="", imageUrl=""),
        Carta(name="Knightmare Unicorn", type="", imageUrl=""),
        Carta(name="Borreload Dragon", type="", imageUrl=""),
        Carta(name="Accesscode Talker", type="", imageUrl=""),
        Carta(name="Apollousa, Bow of the Goddess", type="", imageUrl=""),
        Carta(name="I:P Masquerena", type="", imageUrl=""),
        Carta(name="Linkuriboh", type="", imageUrl=""),
        Carta(name="Crystron Halqifibrax", type="", imageUrl=""),
        Carta(name="Selene, Queen of the Master Magicians", type="", imageUrl=""),
        Carta(name="Saryuja Skull Dread", type="", imageUrl=""),
        Carta(name="Mekk-Knight Crusadia Avramax", type="", imageUrl=""),
        Carta(name="Underworld Goddess of the Closed World", type="", imageUrl=""),
        Carta(name="Splash Mage", type="", imageUrl=""),

// --- Arquetipo "Shaddoll" ---
        Carta(name="El Shaddoll Construct", type="", imageUrl=""),
        Carta(name="El Shaddoll Winda", type="", imageUrl=""),
        Carta(name="Shaddoll Fusion", type="", imageUrl=""),
        Carta(name="Shaddoll Squamata", type="", imageUrl=""),
        Carta(name="Shaddoll Beast", type="", imageUrl=""),

// --- Arquetipo "Burning Abyss" ---
        Carta(name="Dante, Traveler of the Burning Abyss", type="", imageUrl=""),
        Carta(name="Scarm, Malebranche of the Burning Abyss", type="", imageUrl=""),
        Carta(name="Graff, Malebranche of the Burning Abyss", type="", imageUrl=""),
        Carta(name="Cir, Malebranche of the Burning Abyss", type="", imageUrl=""),
        Carta(name="Beatrice, Lady of the Eternal", type="", imageUrl=""),

// --- Arquetipo "Nekroz" ---
    Carta(name="Nekroz of Brionac", type="", imageUrl=""),
    Carta(name="Nekroz of Trishula", type="", imageUrl=""),
    Carta(name="Nekroz of Unicore", type="", imageUrl=""),
    Carta(name="Nekroz Cycle", type="", imageUrl=""),
    Carta(name="Shurit, Strategist of the Nekroz", type="", imageUrl=""),

// --- Arquetipo "Tellarknight" ---
    Carta(name="Satellarknight Deneb", type="", imageUrl=""),
    Carta(name="Satellarknight Altair", type="", imageUrl=""),
    Carta(name="Stellarknight Triverr", type="", imageUrl=""),

// --- Arquetipo "Lightsworn" ---
    Carta(name="Judgment Dragon", type="", imageUrl=""),
    Carta(name="Raiden, Hand of the Lightsworn", type="", imageUrl=""),
    Carta(name="Lumina, Lightsworn Summoner", type="", imageUrl=""),
    Carta(name="Charge of the Light Brigade", type="", imageUrl=""),
    Carta(name="Michael, the Arch-Lightsworn", type="", imageUrl=""),

// --- Arquetipo "HERO" (más) ---
    Carta(name="Destiny HERO - Malicious", type="", imageUrl=""),
    Carta(name="Vision HERO Faris", type="", imageUrl=""),
    Carta(name="Masked HERO Dark Law", type="", imageUrl=""),
    Carta(name="Mask Change", type="", imageUrl=""),
    Carta(name="A Hero Lives", type="", imageUrl=""),

// --- Arquetipo "Cyber Dragon" ---
    Carta(name="Cyber Dragon", type="", imageUrl=""),
    Carta(name="Cyber Dragon Infinity", type="", imageUrl=""),
    Carta(name="Chimeratech Fortress Dragon", type="", imageUrl=""),
    Carta(name="Cyber Emergency", type="", imageUrl=""),
    Carta(name="Cyber Dragon Nova", type="", imageUrl=""),

// --- Cartas "Hand Trap" y Staples Adicionales ---
    Carta(name="Nibiru, the Primal Being", type="", imageUrl=""),
    Carta(name="Ghost Ogre & Snow Rabbit", type="", imageUrl=""),
    Carta(name="D.D. Crow", type="", imageUrl=""),
    Carta(name="Infinite Impermanence", type="", imageUrl=""),
    Carta(name="Called by the Grave", type="", imageUrl=""),
    Carta(name="Crossout Designator", type="", imageUrl=""),
    Carta(name="Forbidden Droplet", type="", imageUrl=""),
    Carta(name="Triple Tactics Talent", type="", imageUrl=""),
    Carta(name="Lightning Storm", type="", imageUrl=""),
    Carta(name="Evenly Matched", type="", imageUrl=""),

// --- Arquetipo "Tearlaments" ---
    Carta(name="Tearlaments Kitkallos", type="", imageUrl=""),
    Carta(name="Tearlaments Reinoheart", type="", imageUrl=""),
    Carta(name="Tearlaments Scheiren", type="", imageUrl=""),

// --- Arquetipo "Kashtira" ---
    Carta(name="Kashtira Fenrir", type="", imageUrl=""),
    Carta(name="Kashtira Unicorn", type="", imageUrl=""),
    Carta(name="Kashtira Arise-Heart", type="", imageUrl=""),
    Carta(name="Number 89: Diablosis the Mind Hacker", type="", imageUrl=""),

// --- Arquetipo "Spright" ---
    Carta(name="Spright Elf", type="", imageUrl=""),
    Carta(name="Spright Blue", type="", imageUrl=""),
    Carta(name="Gigantic Spright", type="", imageUrl=""),

// --- Arquetipo "Branded" ---
    Carta(name="Aluber the Jester of Despia", type="", imageUrl=""),
    Carta(name="Branded Fusion", type="", imageUrl=""),
    Carta(name="Mirrorjade the Iceblade Dragon", type="", imageUrl=""),
    Carta(name="Lubellion the Searing Dragon", type="", imageUrl=""),
    Carta(name="Fallen of Albaz", type="", imageUrl=""),

// --- Monstruos "Número" (Number) Adicionales ---
    Carta(name="Number 107: Galaxy-Eyes Tachyon Dragon", type="", imageUrl=""),
    Carta(name="Number 97: Draglubion", type="", imageUrl=""),
    Carta(name="Number 100: Numeron Dragon", type="", imageUrl=""),
    Carta(name="Number 41: Bagooska the Terribly Tired Tapir", type="", imageUrl=""),
    Carta(name="Number 38: Hope Harbinger Dragon Titanic Galaxy", type="", imageUrl=""),

// --- Cartas Variadas y Populares ---
    Carta(name="Pot of Desires", type="", imageUrl=""),
    Carta(name="Pot of Extravagance", type="", imageUrl=""),
    Carta(name="There Can Be Only One", type="", imageUrl=""),
    Carta(name="Skill Drain", type="", imageUrl=""),
    Carta(name="Gozen Match", type="", imageUrl=""),
    Carta(name="Rivalry of Warlords", type="", imageUrl=""),
    Carta(name="Super Polymerization", type="", imageUrl=""),
    Carta(name="Dark Ruler No More", type="", imageUrl=""),
    Carta(name="Fusion Destiny", type="", imageUrl=""),
    Carta(name="Red-Eyes Dark Dragoon", type="", imageUrl=""),
    Carta(name="Destiny HERO - Destroyer Phoenix Enforcer", type="", imageUrl=""),
    Carta(name="Verte Anaconda", type="", imageUrl=""),
    Carta(name="Zoodiac Drident", type="", imageUrl=""),
    Carta(name="Elder Entity N'tss", type="", imageUrl=""),
    Carta(name="Fossil Dyna Pachycephalo", type="", imageUrl="")

    )
}
