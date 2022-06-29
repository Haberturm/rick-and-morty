package com.haberturm.rickandmorty.presentation.common

import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.haberturm.rickandmorty.util.applyWithDisabledTextWatcher

class FiltersMethods {

    fun setUpDropDownMenu(
        menu: AutoCompleteTextView,
        adapter: ArrayAdapter<String>,
        onItemSelected: (Int) -> Unit,
        selectedPositionState: LiveData<Int>,
        lifecycleOwner: LifecycleOwner
    ){
        menu.apply {
            setAdapter(adapter)
            selectedPositionState.observe(lifecycleOwner){
                setText(adapter.getItem(it).toString(), false)
            }
            setOnItemClickListener { _, _, position, _ ->
                onItemSelected(position)
            }
        }
    }

    fun setUpFilterEditText(
        editText: EditText,
        onTextChanged: (CharSequence?) -> Unit,
        textState: LiveData<String>,
        lifecycleOwner: LifecycleOwner
    ){
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                onTextChanged(p0)
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }

        editText.addTextChangedListener(textWatcher)

        textState.observe(lifecycleOwner){ text->
            editText.apply {
                applyWithDisabledTextWatcher(textWatcher){
                    setText(text)
                    setSelection(length())
                }
            }
        }
    }
}