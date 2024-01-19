package com.example.apiintegration

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.apiintegration.ui.theme.ApiIntegrationTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIInterface::class.java)

        val retrofitData = retrofitBuilder.getProductData()

        retrofitData.enqueue(object : Callback<MyData?> {
            override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {
                var responseBody = response.body()
                val productList = responseBody?.products

                val collectDataInSB = StringBuilder()

                if (productList != null) {
                    for(myData in productList){
                        collectDataInSB.append(myData.title + "  ")
                    }
                    }

                setContent {
                    MainScreen(collectDataInSB.toString())
                }

                }

            override fun onFailure(call: Call<MyData?>, t: Throwable) {
                Log.d("Main Activity", "OnFailure: " + t.message)

            }
        })


        }

}

@Composable
fun MainScreen(title: String) {
//    CardList()

    Column(modifier = Modifier.fillMaxSize()){
        Text(text = title)
    }
}


@Composable
fun CardList() {
    val cardDataList = listOf(
        CardData("Title 1", "Description 1"),
        CardData("Title 2", "Description 2"),
        // Add more card data as needed
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(cardDataList) { cardData ->
            CardItem(cardData)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CardItem(cardData: CardData) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = cardData.title,
                style = typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = cardData.description,
                style = typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

data class CardData(val title: String, val description: String)