package pfhb.damian.dragginwindow

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Typeface
import android.transition.Slide
import android.util.Log
import android.view.*
import android.view.View.*
import androidx.appcompat.app.AppCompatActivity
import android.content.SharedPreferences
import android.widget.*
import com.google.gson.GsonBuilder
import android.widget.TextView
import android.os.Bundle




object ModelPreferencesManager {

    //Shared Preference field used to save and retrieve JSON string
    lateinit var preferences: SharedPreferences

    //Name of Shared Preference file
    private const val PREFERENCES_FILE_NAME = "PREF_SHELLS"

    /**
     * Call this first before retrieving or saving object.
     *
     * @param application Instance of application class
     */
    fun with(application: MainActivity) {
        preferences = application.getSharedPreferences(
            PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Saves object into the Preferences.
     *
     * @param `object` Object of model class (of type [T]) to save
     * @param key Key with which Shared preferences to
     **/
    fun <T> put(`object`: T, key: String) {
        //Convert object to JSON String.
        val jsonString = GsonBuilder().create().toJson(`object`)
        //Save that String in SharedPreferences
        preferences.edit().putString(key, jsonString).apply()
    }

    /**
     * Used to retrieve object from the Preferences.
     *
     * @param key Shared Preference key with which object was saved.
     **/
    inline fun <reified T> get(key: String): T? {
        //We read JSON String which was saved.
        val value = preferences.getString(key, null)
        //JSON String was found which means object can be read.
        //We convert this JSON String to model object. Parameter "c" (of
        //type Class < T >" is used to cast.
        return GsonBuilder().create().fromJson(value, T::class.java)
    }
}



class TextExp(val text:String, val color: Int)

class Shell(){
    var lote = arrayListOf<TextExp>()

    fun add(shell: ArrayList<TextExp>){
        lote = shell
    }
}

class MainActivity : AppCompatActivity() {

    var ListOfTextExp = arrayListOf<TextExp>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ModelPreferencesManager.with(this)

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener{
            ListOfTextExp.clear()

            shellCreator()
            popUp("Nowe okienko", ListOfTextExp, "X")
        }
        val buttonLoad = findViewById<Button>(R.id.buttonLoad)
        buttonLoad.setOnClickListener {
            LoadShells()
        }
        val buttonClear = findViewById<Button>(R.id.buttonClear)
        buttonClear.setOnClickListener{
            ClearShells()
        }
    }
    fun ClearShells(){
        Log.d(TAG, "CLEAR DATA INIT")
        application.getSharedPreferences("PREF_SHELLS", Context.MODE_PRIVATE).edit().clear().apply()
    }

    fun SaveShells(shellS : Shell){
        Log.d(TAG, "SAVE INIT")
        var packed = ModelPreferencesManager.get<PackageOfShells>("SHELLS")
        if (packed != null) {
            Toast.makeText(baseContext, "Successfully loaded data", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "SAVE: $packed")
        }
        if (packed == null) {
            packed = PackageOfShells(arrayListOf(shellS))
        } else
        packed.add(shellS)

        ModelPreferencesManager.put(packed, "SHELLS")
    }

    fun LoadShells(){
        Log.d(TAG, "LOAD INIT")
        val pckd = ModelPreferencesManager.get<PackageOfShells>("SHELLS") ?: return
        var packed = PackageOfShells(pckd.get())

        for(iter in packed.get()){
            popUp("Wczytane okienko", iter.lote, "X")
            Log.d(TAG, "SAVE: WCZYTANO OKIENKO")
        }
    }

    fun popUp(_title : String, _desc : ArrayList<TextExp>, _button : String) {
        val tempShell = Shell()
        tempShell.add(_desc)
        val mLayoutInflater : LayoutInflater = LayoutInflater.from(baseContext)
        val mView = mLayoutInflater.inflate(R.layout.activity_pop_up_window,
            null)
        mView.findViewById<TextView>(R.id.popup_window_title).text = _title
        val llView = mView.findViewById<LinearLayout>(R.id.window_ll_desc)

        // Adding shell options from list created before
        for(iter in _desc){
            val newTextView = TextView(this)
            with(newTextView) {
                setTextColor(iter.color)
                text = iter.text
                textSize = 11F
                textAlignment = TEXT_ALIGNMENT_CENTER
                typeface = Typeface.createFromAsset(assets, "tahoma.ttf")
            }

            llView.addView(newTextView)
        }
        val btn = mView.findViewById<Button>(R.id.popup_window_button)
        val btnSave = mView.findViewById<Button>(R.id.popup_window_save)
        btn.text = _button


        val mPopupWindow = PopupWindow(
            mView,
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false
        )

        val slideIn = Slide()
        slideIn.slideEdge = Gravity.TOP
        mPopupWindow.enterTransition = slideIn

        // Slide animation for popup window exit transition
        val slideOut = Slide()
        slideOut.slideEdge = Gravity.BOTTOM
        mPopupWindow.exitTransition = slideOut

        // Algorithm to move window with finger
        mPopupWindow.showAtLocation(findViewById(R.id.mainView), Gravity.CENTER, 0, 0);
        mView.setOnTouchListener(object : OnTouchListener {
            private var dx = 0
            private var dy = 0
            private var ox = 0
            private var oy = 0
            override fun onTouch(view: View?, motionEvent: MotionEvent): Boolean {
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        dx +=  motionEvent.rawX.toInt() -ox
                        dy +=  motionEvent.rawY.toInt() -oy
                    }
                    MotionEvent.ACTION_UP -> {
                        ox = motionEvent.rawX.toInt()
                        oy = motionEvent.rawY.toInt()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val x = motionEvent.rawX.toInt()
                        val y = motionEvent.rawY.toInt()
                        val left = x - dx
                        val top = y - dy
                        mPopupWindow.update(left, top, -1, -1, true)
                    }
                }
                return true
            }
        })
        // Close window with button
        btn.setOnClickListener {
            mPopupWindow.dismiss()
        }
        btnSave.setOnClickListener{
            SaveShells(tempShell)
        }
    }

    fun shellCreator(){
        Log.d(TAG, "Checkpoint Done")
        val ListOfOptions = arrayListOf<ShellOptions>()
        var shell1 : ShellOptions? = null
        val counts = (5..10).random()
        for(i in 1..counts) {
            shell1 = null
            while (shell1?.isValid == false || shell1 == null) {

                shell1 = ShellOptions()
                shell1.create()
                if(shell1.isValid && i>0)
                    for(item in ListOfOptions){
                        if( (item.Order==3 && shell1?.Order == 3) || (item.Order==4 && shell1?.Order == 4)) shell1 = null
                        if(item.type() == shell1?.type())
                            shell1 = null
                    }
            }

            ListOfOptions.add(shell1)
        }
        Log.d(TAG, "Checkpoint before")
        for(i in 1..5)
            ShellOptSort(ListOfOptions, i)

        ListOfOptions.clear()
    }
    private fun ShellOptSort(ListOfOptions: ArrayList<ShellOptions>, sort:Int) {
        val IntRarity = arrayListOf("C", "B", "A")
        if(sort == 2){
            for(r in 0..2)
                for(shell in ListOfOptions)
                    if(shell.rarity == IntRarity[r] && shell.Order == 2)
                        ListOfTextExp.add(TextExp(shell.toString(), shell.color()))
        }else
        if (ListOfOptions.isNotEmpty())
            for (shell in ListOfOptions)
                if (shell.Order == sort)
                    ListOfTextExp.add(TextExp(shell.toString(), shell.color()))
    }


}

data class PackageOfShells(val shells: ArrayList<Shell>){
    fun add(shell : Shell){
        shells.add(shell)
    }
    fun get():ArrayList<Shell>{
        return shells
    }


}