import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Photo
import com.example.myapplication.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var photoAdapter: PhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apicall)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        photoAdapter = PhotoAdapter()
        recyclerView.adapter = photoAdapter

        fetchPhotos()
    }

    private fun fetchPhotos() {
        GlobalScope.launch(Dispatchers.IO) {
            val json = URL("https://jsonplaceholder.typicode.com/photos").readText()
            val jsonArray = JSONArray(json)
            val photoList = mutableListOf<Photo>()

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val photo = Photo(
                    jsonObject.getInt("id"),
                    jsonObject.getString("title"),
                    jsonObject.getString("url")
                )
                photoList.add(photo)
            }

            withContext(Dispatchers.Main) {
                photoAdapter.setPhotos(photoList)
            }
        }
    }
}
