package com.king.kingbit.android.presentation.home.crypto.components

import androidx.compose.runtime.Composable

//@Composable
//fun PriceChange(
//    change: DisplayableNumber,
//    modifier: Modifier = Modifier
//) {
//    val contentColor = if(change.value < 0.0) {
//        MaterialTheme.colorScheme.onErrorContainer
//    } else {
//        Color.Green
//    }
//    val backgroundColor = if(change.value < 0.0) {
//        MaterialTheme.colorScheme.errorContainer
//    } else {
//        greenBackground
//    }
//
//    Row(
//        modifier = modifier
//            .clip(RoundedCornerShape(100f))
//            .background(backgroundColor)
//            .padding(horizontal = 4.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Icon(
//            imageVector = if(change.value < 0.0) {
//                Icons.Default.KeyboardArrowDown
//            } else {
//                Icons.Default.KeyboardArrowUp
//            },
//            contentDescription = null,
//            modifier = Modifier.size(20.dp),
//            tint = contentColor
//        )
//        Text(
//            text = "${change.formatted} %",
//            color = contentColor,
//            fontSize = 14.sp,
//            fontWeight = FontWeight.Medium
//        )
//    }
//}