package com.rehan.pmob4

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rehan.pmob4.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var appExecutor: AppExecutor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appExecutor = AppExecutor()
        val barangId = intent.getIntExtra("barang_id",-1)
        if(barangId != -1){
            appExecutor.diskIO.execute {
                val dao = DatabaseBarang.getDatabase(this@DetailActivity).barangDao()
                val selectedBarang = dao.getBarangById(barangId)
                binding.apply {
                    etNama.setText(selectedBarang.nama)
                    etJenis.setText(selectedBarang.jenis)
                    etharga.setText(selectedBarang.harga.toString())

                    btnUpdate.setOnClickListener {
                        val updateBarang = selectedBarang.copy(
                            nama = etNama.text.toString(),
                            jenis = etJenis.text.toString(),
                            harga = etharga.text.toString().toInt()
                        )
                        appExecutor.diskIO.execute {
                         dao.update(updateBarang)
                     }
                    }
                    btnDelete.setOnClickListener {
                        appExecutor.diskIO.execute {
                            dao.delete(selectedBarang)
                            finish()
                        }
                    }
                }
            }
        }

    }
}