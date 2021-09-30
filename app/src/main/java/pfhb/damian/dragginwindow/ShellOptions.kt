package pfhb.damian.dragginwindow

import android.content.ContentValues
import android.graphics.Color
import android.util.Log

class ShellOptions {
    private var additional : String = ""
    private var value : Int = 0
    private var color : Int = Color.rgb(0, 0, 0)
    private var desc : String = ""
    private var type_temp : String = ""
    private var type: String = ""
    var rarity: String = ""
    var Order: Int = -1
    var isValid : Boolean = true
    private val ListOfTypes = arrayListOf<String>(
        "SL Damage",
        "SL Defense",
        "SL Property",
        "SL Energy",
        "SL Overall",
        "Enhanced",
        "%Damage",
        "Pvp dmg",
        "Minor Bleeding",
        "Open Wound",
        "Heavy Bleeding",
        "Stun",
        "Freeze",
        "Stun Badly",
        "DmgTo:Plant",
        "DmgTo:Animal",
        "DmgTo:monsters",
        "DmgTo:Undead",
        "DmgTo:Lower Society Monsters",
        "MapBoss",
        "Property:Light",
        "Property:Fire",
        "Property:Water",
        "Property:Shadow",
        "EleProp",
        "MP",
        "Recovery:HP",
        "Recovery:MP",
        "EXP",
        "CEXP",
        "PVPDefence",
        "ResPVP:Fire",
        "ResPVP:Shadow",
        "ResPVP:Light",
        "ResPVP:Water",
        "ResPVP:All",
        "MPPVP",
        "CritChance",
        "CritDmg",
        "Undisturbed"
        )
    private val ListOfRarity = arrayListOf<String>(
        "S",
        "A",
        "B",
        "C"
    )

    fun create(){
        type = ListOfTypes[(0 until ListOfTypes.size).random()]
        rarity = ListOfRarity[(0 until ListOfRarity.size).random()]
        Log.d(ContentValues.TAG, "TYPE: $type     RARITY: $rarity")

        type_temp = type
        var type_arr = ArrayList<String>()
        if(type.contains("SL")) {
            type_arr = type.split(" ") as ArrayList<String>
            type = "SL"
        }
        if(type.contains("DmgTo:")){
            type_arr = type.split(":") as ArrayList<String>
            type = "DmgTo"
        }
        if(type.contains("Property:")){
            type_arr = type.split(":") as ArrayList<String>
            type = "Property"
        }
        if(type.contains("Recovery")){
            type_arr = type.split(":") as ArrayList<String>
            type = "Recovery"
        }
        if(type.contains("ResPVP:")){
            type_arr = type.split(":") as ArrayList<String>
            type = "ResPVP"
        }
        when(type){
            "SL" -> {
                if(type_arr[1].equals("Overall")){
                    desc = "Increased Overall SL Stat"
                    rarity = "S"
                }else{
                    desc = "Increased SL ${type_arr[1]} Stat"
                    if(rarity == "S") rarity = "B"
                }
                Order = 2
                type = type_temp
                additional = ""
                when(rarity) {
                    "A" -> {
                        value = (11..17).random()
                        color = ShellColor.A
                    }
                    "B" -> {
                        value = (6..11).random()
                        color = ShellColor.B
                    }
                    "S" -> {
                        Order = 3
                        value = (9..11).random()
                        color = ShellColor.S
                    }
                    else -> {
                        isValid = false
                    }
                }
            }
            "%Damage" -> {
                Order = 3
                rarity = "S"
                desc = "% to Damage"
                value = (10..19).random()
                color = ShellColor.S
                additional = "%"
            }
            "Enhanced" -> {
                Order = 2
                desc = "Enhanced Damage"
                when(rarity) {
                    "C" -> {
                        value = (60..95).random()
                        color = ShellColor.C
                    }
                    "B" -> {
                        value = (95..142).random()
                        color = ShellColor.B
                    }
                    "A" -> {
                        value = (142..190).random()
                        color = ShellColor.A
                    }
                    else -> isValid = false
                }
            }
            "Pvp dmg" -> {
                Order = 5
                desc = "% to Damage in PVP"
                color = ShellColor.PVP
                additional = "%"

                when(rarity){
                    "C" -> value =(7..11).random()
                    "B" -> value =(11..14).random()
                    "A" -> value =(14..17).random()
                    "S" -> value =(17..33).random()
                    else -> isValid = false
                }
            }
            "Freeze" -> {
                Order = 2
                rarity = "B"
                value = (2..4).random()
                desc = "Freeze"
                additional = "%"
                color = ShellColor.B
            }
            "Open Wound" -> {
                Order = 2
                rarity = "B"
                value = (2..4).random()
                desc = "Open Wound"
                additional = "%"
                color = ShellColor.B
            }
            "Minor Bleeding" -> {
                Order = 2
                rarity = "C"
                value = (2..4).random()
                desc = "Minor Bleeding"
                additional = "%"
                color = ShellColor.C
            }
            "Stun Badly" -> {
                Order = 2
                rarity = "A"
                value = (2..4).random()
                desc = "Stun Badly"
                additional = "%"
                color = ShellColor.A
            }
            "Heavy Bleeding" -> {
                Order = 2
                rarity = "A"
                value = 1
                desc = "Heavy Bleeding"
                additional = "%"
                color = ShellColor.A
            }
            "Stun" -> {
                Order = 2
                rarity = "C"
                value = (2..4).random()
                desc = "Stun"
                additional = "%"
                color = ShellColor.C
            }
            "DmgTo" -> {
                Order = 2
                desc = "Increased Damage to ${type_arr[1]}"
                additional = "%"
                type = type_temp

                when(rarity){
                    "C" -> {
                        color = ShellColor.C
                        value = (7..9).random()
                    }
                    "B" -> {
                        color = ShellColor.B
                        value = (8..19).random()
                    }
                    else -> isValid = false
                }
            }
            "MapBoss" -> {
                Order = 3
                rarity = "S"
                color = ShellColor.S
                desc = "Increases damage to map bosses"
                additional = "%"
                value = (15..23).random()
            }
            "Property" -> {
                Order = 2
                desc = "Increased ${type_arr[1]} Property"
                type = type_temp
                additional = ""
                when(rarity){
                    "B" -> {
                        color = ShellColor.B
                        value = (50..76).random()
                    }
                    "A" -> {
                        color = ShellColor.A
                        value = (76..142).random()
                    }
                    else -> isValid = false
                }
            }
            "EleProp" -> {
                Order = 3
                rarity = "S"
                color = ShellColor.S
                desc = "Increased Elemental Properties"
                additional = ""
                value = (140..153).random()
            }
            "MP" -> {
                Order = 2
                desc = "Reduced MP Consumption"
                additional = "%"
                when(rarity){
                    "C" -> {
                        color = ShellColor.C
                        value = (7..11).random()
                    }
                    "B" -> {
                        color = ShellColor.B
                        value = (11..21).random()
                    }
                    else -> isValid = false
                }
            }
            "Recovery" -> {
                Order = 2
                desc = "${type_arr[1]} Recovery per Kill"
                type = type_temp
                additional = ""
                when(rarity){
                    "B" -> {
                        color = ShellColor.B
                        value = (100..142).random()
                    }
                    "A" -> {
                        color = ShellColor.A
                        value = (142..190).random()
                    }
                    else -> isValid = false
                }
            }
            "Gold" -> {
                Order = 4
                desc = "Gain More Gold"
                additional = "%"
                color = ShellColor.MISC
                when(rarity){
                    "C" -> value = (6..9).random()
                    "B" -> value = (9..17).random()
                    "A" -> value = (17..33).random()
                    else -> isValid = false
                }
            }
            "EXP" -> {
                Order = 4
                desc = "Increased combat EXP"
                additional = "%"
                color = ShellColor.MISC
                when(rarity){
                    "B" -> value = (7..9).random()
                    "A" -> value = (9..14).random()
                    else -> isValid = false
                }
            }
            "CEXP" -> {
                Order = 4
                desc = "Gain more CXP"
                additional = "%"
                color = ShellColor.MISC
                when(rarity){
                    "B" -> value = (7..9).random()
                    "A" -> value = (9..14).random()
                    else -> isValid = false
                }
            }
            "PVPDefence" -> {
                Order = 5
                desc = "Reduces opponent's defence in PvP by"
                additional = "%"
                color = ShellColor.PVP

                when(rarity){
                    "C" -> value = (8..11).random()
                    "B" -> value = (11..14).random()
                    "A" -> value = (14..19).random()
                    "S" -> value = (19..33).random()
                }
            }
            "ResPVP" -> {
                Order = 6
                desc = "Reduce opponent's ${type_arr[1]} Resistance in PvP"
                type = type_temp
                if(type_arr[1].contains("All")) rarity = "S"
                color = ShellColor.PVP
                additional = "%"
                when(rarity){
                    "B" -> value = (5..7).random()
                    "A" -> value = (7..14).random()
                    "S" -> {
                        Order = 5
                        value = (10..17).random()
                    }
                    else -> isValid = false
                }
            }
            "MPPVP" -> {
                Order = 6
                desc = "Drain Opponent's Mana in PvP"
                additional = ""
                color = ShellColor.PVP
                when(rarity){
                    "C" -> value = (10..19).random()
                    "B" -> value = (19..28).random()
                    "A" -> value = (28..47).random()
                    else -> isValid = false
                }
            }
            "CritDmg" -> {
                Order = 1
                desc = "Increased Critical Damage"
                additional = "%"
                color = ShellColor.C
                rarity = "C"
                value = (25..57).random()
            }
            "CritChance" -> {
                Order = 1
                desc = "Increased Chance of Critical Hit"
                additional = "%"
                color = ShellColor.C
                rarity = "C"
                value = (3..9).random()
            }
            "Undisturbed" -> {
                Order = 1
                desc = "Undisturbed When Casting Spells"
                additional = ""
                value = -1
                color = ShellColor.C
                rarity = "C"
            }
            else -> isValid = false
        }
    }

    override fun toString(): String {
        return "$rarity-$desc${if(value>0) ": $value" else ""}$additional"
    }
    fun color() : Int{
        return color
    }
    fun type() : String{
        return type
    }
}