package com.example.simplecontactlist.ui.view

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.simplecontactlist.ListState
import com.example.simplecontactlist.MyViewModel
import com.example.simplecontactlist.R

@Composable
fun AddContact(
    state: ListState,
    modifier: Modifier,
    viewModel: MyViewModel
) {
    Column(
        modifier = modifier
            .animateContentSize()
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(all = 20.dp)
    ) {
        Text(
            text = "Add new contact",
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            value = state.name,
            onValueChange = { name ->
                viewModel.onChangeNameFieldValue(name)
                viewModel.clearErrorText()
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            label = { Text(stringResource(R.string.Add_contact_name_field_label)) },
            supportingText = {
                if (state.name.isEmpty()) {
                    Text(
                        text = state.errorText ?: "",
                        color = Color.Red
                    )
                }
            },
            maxLines = 1
        )

        OutlinedTextField(
            value = state.surName,
            onValueChange = { surName ->
                viewModel.onChangeSurnameValue(surName)
                viewModel.clearErrorText()
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            label = { Text(stringResource(R.string.Add_contact_surname_field_label)) },
            supportingText = {
                if (state.surName.isEmpty()) {
                    Text(
                        text = state.errorText ?: "",
                        color = Color.Red
                    )
                }
            },
            maxLines = 1
        )

        OutlinedTextField(
            value = state.number,
            onValueChange = { number ->
                viewModel.onChangeNumberValue(number)
                viewModel.clearErrorText()
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            label = { Text(stringResource(R.string.Add_contact_number_field_label)) },
            supportingText = {
                if (state.number.isEmpty()) {
                    Text(
                        text = state.errorText ?: "",
                        color = Color.Red
                    )
                }
            },
            maxLines = 1
        )

        val text = stringResource(R.string.emty_name_field_error)
        Button(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally),
            onClick = { viewModel.addItemToList(emptyStateStringResource = text) }
        ) { Text(stringResource(R.string.add_contact_button_text)) }

    }
}
