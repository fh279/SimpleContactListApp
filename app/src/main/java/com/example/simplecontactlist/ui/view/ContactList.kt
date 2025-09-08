package com.example.simplecontactlist.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplecontactlist.ListState
import com.example.simplecontactlist.viewModel.MyViewModel
import com.example.simplecontactlist.R
import kotlin.random.Random

@Composable
fun ContactList(
    modifier: Modifier,
    state: ListState,
    viewModel: MyViewModel
) {
    LazyColumn(
        state = rememberLazyListState(),
        modifier = modifier
            .fillMaxSize()
            .height(350.dp)
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(5.dp)
            )
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
    ) {
        if (state.listItems.isNotEmpty()) {
            items(
                state.listItems,
                key = { it.id }
            ) { item ->
                val color = remember {
                    Color(
                        red = Random.nextFloat(),
                        green = Random.nextFloat(),
                        blue = Random.nextFloat()
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.Absolute.Left,
                    modifier = Modifier
                        .padding(start = 12.dp, end = 12.dp, top = 10.dp)
                        .fillMaxWidth()
                        .background(color = Color.White, shape = RoundedCornerShape(5.dp)),
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(color = color),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.name.getOrNull(0).toString(),
                            color = Color.White,
                            modifier = Modifier.align(Alignment.Center),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.width(250.dp).horizontalScroll(rememberScrollState())) {
                        Text(
                            text = "${item.name} ${item.surname}",
                            maxLines = 1,
                        )
                        Text(
                            text = item.number,
                            maxLines = 1
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = { viewModel.removeItemFromList(item) },
                        modifier = Modifier,
                        shape = MaterialTheme.shapes.extraSmall,
                        contentPadding = PaddingValues(
                            start = 2.dp,
                            top = 2.dp,
                            end = 2.dp,
                            bottom = 2.dp
                        )
                    ) { Text(stringResource(R.string.delete_button_text)) }
                }
            }
        } else {
            items(arrayListOf("")) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.emptystate2),
                        contentDescription = "EmptyState",
                        modifier = Modifier
                            .size(170.dp)
                            .clip(CircleShape),
                    )
                    Text(
                        text = stringResource(R.string.empty_state_text),
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}
