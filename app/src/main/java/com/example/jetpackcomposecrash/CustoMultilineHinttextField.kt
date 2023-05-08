package com.example.jetpackcomposecrash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun CustomMultilineTextField(
    value: String,
    onValueChanged:(String) -> Unit,
    modifier: Modifier=Modifier,
    hintText:String="",
    textStyle: androidx.compose.ui.text.TextStyle= MaterialTheme.typography.bodyMedium,
    maxLines:Int=4
){
    BasicTextField(
        value = value,
        onValueChange = onValueChanged,
        textStyle=textStyle,
        maxLines=maxLines,
        decorationBox = {innerTextField ->  
            Box(modifier =  modifier){
                if (value.isEmpty()){
                    Text(
                        text = hintText,
                        color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                     )
                }
                innerTextField()
            }
        })
}