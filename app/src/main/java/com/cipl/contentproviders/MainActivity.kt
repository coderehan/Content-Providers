package com.cipl.contentproviders

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.cipl.contentproviders.databinding.ActivityMainBinding

// Eg : Basically whatsapp access contact app to fetch the numbers.
// Whatsapp uses content resolver to request contact app inorder to fetch its data.
// Contact app uses content providers to approve the whatsapp request and give its database access to whatsapp.

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val list:MutableLiveData<MutableSet<String>> = MutableLiveData()

    private val registerActivityForResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){    // If its true, we can access contact list from contact app
                list.postValue(getContactList())
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        list.postValue(getContactList())
        list.observe(this){
            it?.let{
                binding.rvContacts.adapter = AdapterContactList(it.toList())
            }
        }
    }

    @SuppressLint("Range", "Recycle")
    fun getContactList():MutableSet<String>{
        val set = mutableSetOf<String>()
        sdkIntAboveOreo {
            isPermissionGranted(this,android.Manifest.permission.READ_CONTACTS){
                if(it){     // If permission granted then we will access contact list
                    // Our app uses content resolver to get the permission from contact app
                    val contentResolver = applicationContext.contentResolver
                    val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null)
                    if(cursor?.moveToFirst()==true){
                        do {
                            // Getting contact name from contact app
                            val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                            set.add(name)   // we add all our contact names list in set
                        }while(cursor.moveToNext())
                    }
                }else{
                    registerActivityForResult.launch(android.Manifest.permission.READ_CONTACTS)
                }
            }
        }
        return set
    }


    // First we have to get the runtime permission if it is >= oreo
    // First we have to check our build version number
    private inline fun sdkIntAboveOreo(call:()->Unit){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            call.invoke()
        }
    }

    private inline fun isPermissionGranted(context: Context, permission: String, call:(Boolean) -> Unit){
        if(ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED){
            call.invoke(true)
        }else{
            call.invoke(false)
        }
    }


}