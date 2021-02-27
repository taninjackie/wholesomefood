package com.egco.wholesomefoodapp

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.setting_activity.*
import java.io.*
import java.util.*
import kotlin.math.log

class SettingActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting_activity)
        val username  = intent.getStringExtra("username").toString()
        val deleteUser = { dialog: DialogInterface, which: Int ->
            var usernameBackup:String = ""
            applicationContext.openFileInput("users.txt").bufferedReader().useLines { lines ->
                usernameBackup = lines.fold("") { some, text -> "$text" }
            }
            val listUserBackup = usernameBackup.split(",").toMutableList()
            deleteFile("users.txt")
            for(i in listUserBackup.indices)
            {
                if(listUserBackup[i] == username)
                {
                    listUserBackup[i] = ""
                }
                if(listUserBackup[i] != "")
                {
                    save("users.txt",listUserBackup[i]+",")
                }
            }
            deleteFile("$username.json")
            startActivity(Intent(this,MainActivity::class.java))
        }
        deleteUserBT.setOnClickListener {
            val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogCustomDelete))

            with(builder)
            {
                setTitle("Are you sure to delete ?")
                setPositiveButton(
                    "OK",
                    DialogInterface.OnClickListener(function = deleteUser)
                )
                setNegativeButton("Cancel") { dialog: DialogInterface, which: Int -> }

                show()
            }


        }
    }


    private fun save(file:String,text:String) {
        var fos: FileOutputStream? = null
        try {
            fos = openFileOutput(file, MODE_APPEND)
            fos.write(text.toByteArray())
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
    private fun getJson(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.openFileInput(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }
    fun withEditText(view: View) {
        val builder = MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
        val inflater = layoutInflater
        builder.setTitle("Change Name-Surname")
        val dialogLayout = inflater.inflate(R.layout.alert_dialog_with_edittext, null)
        val editText  = dialogLayout.findViewById<EditText>(R.id.editText)
        builder.setView(dialogLayout)
        builder.setPositiveButton("OK") { dialogInterface, i ->
            val username  = intent.getStringExtra("username").toString()
            val user:String = getJson(applicationContext,"$username.json").toString()
            val userModel = Gson().fromJson(user, UserModel::class.java)
            if(editText.text.toString() != "")
            {
                userModel.name = editText.text.toString()
                deleteFile("$username.json")
                val gsonPretty = GsonBuilder().setPrettyPrinting().create()
                val json: String = gsonPretty.toJson(userModel)
                save("$username.json", json)
                Toast.makeText(this, "Change Name Success", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Please Fill Your Name-Surname", Toast.LENGTH_SHORT).show()
            }

        }
        builder.show()
    }
    fun withMultiChoiceList(view: View) {
        val items = arrayOf("Shrimp", "Salmon", "Apple", "Tomato", "Pumpkin", "Cucumber", "Broccoli", "Rambutan")
        val selectedList = ArrayList<Int>()
        val builder = AlertDialog.Builder(this,R.style.AlertDialogTheme)

        builder.setTitle("Food Allergy")
        builder.setMultiChoiceItems(items, null
        ) { dialog, which, isChecked ->
            if (isChecked) {
                selectedList.add(which)
            } else if (selectedList.contains(which)) {
                selectedList.remove(Integer.valueOf(which))
            }
        }

        builder.setPositiveButton("DONE") { dialogInterface, i ->
            val selectedStrings = ArrayList<String>()

            for (j in selectedList.indices) {
                selectedStrings.add(items[selectedList[j]])
            }
            val username  = intent.getStringExtra("username").toString()
            val user:String = getJson(applicationContext,"$username.json").toString()
            val userModel = Gson().fromJson(user, UserModel::class.java)
            userModel.foodallergy = selectedStrings
            deleteFile("$username.json")
            val gsonPretty = GsonBuilder().setPrettyPrinting().create()
            val json: String = gsonPretty.toJson(userModel)
            save("$username.json", json)
            Toast.makeText(this, "Change Food Allergy Success", Toast.LENGTH_SHORT).show()

        }

        builder.show()

    }

}