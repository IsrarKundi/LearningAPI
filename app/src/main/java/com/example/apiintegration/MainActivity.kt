package com.example.apiintegration

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.apiintegration.ui.theme.ApiIntegrationTheme
import com.squareup.picasso.Picasso
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

            // On Successful Response
            override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {
                var responseBody = response.body()
                val productList = responseBody?.products

                productList?.let {
                    setContent {
                        MainScreen(products = it)
                    }
                }

                }

            // In Case Of Failure
            override fun onFailure(call: Call<MyData?>, t: Throwable) {
                Log.d("Main Activity", "OnFailure: " + t.message)

            }
        })


        }

}

@Composable
fun MainScreen(products: List<Product>) {
    // Assuming you have a List<Product> from your API response
    ProductList(products = products)
}
//

@Composable
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.LightGray)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
        ) {


//            Image(painter = product.thumbnail, contentDescription = "product Image")
            Column(
                modifier = Modifier
                    .width(120.dp) // Increase width
                    .height(200.dp) // Increase height
                    .padding(vertical = 50.dp, horizontal = 10.dp)
            ) {


                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(product.thumbnail)
                        .crossfade(true) // Enable smooth fade-in
                        .build(),
                    contentDescription = "Image description for accessibility",
                    placeholder = painterResource(androidx.core.R.drawable.ic_call_decline), // Optional placeholder
                    error = painterResource(androidx.core.R.drawable.ic_call_answer_low),
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = product.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "$${product.description}", fontSize = 10.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Price: $${product.price}", fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Rating: ${product.rating}", fontSize = 12.sp)
            }

        }


    }
}
@Composable
fun ProductList(products: List<Product>) {
    LazyColumn {
        items(products) { product ->
            ProductCard(product = product)
        }
    }
}

data class CardData(val title: String, val description: String)