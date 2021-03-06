package br.com.greenlight.ui.user.register

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.com.greenlight.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.user_register_fragment.*

class UserRegisterFragment : Fragment() {

    private lateinit var registerViewModel: UserRegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        registerViewModel = ViewModelProvider(this).get(UserRegisterViewModel::class.java)

        registerViewModel.status.observe(viewLifecycleOwner){
            if(it)
                findNavController().popBackStack()
        }

        registerViewModel.msg.observe(viewLifecycleOwner){
            if(!it.isNullOrBlank())
                Snackbar.make(requireContext(), this.requireView(), it, Snackbar.LENGTH_LONG).show()
        }

        return inflater.inflate(
            R.layout.user_register_fragment,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnCadastro.setOnClickListener {
            val password = editTextPassword.text.toString()
            val repeatPassword = editTextRepeatPassword.text.toString()

            if (password == repeatPassword) {
                val email = editTextCadastroEmail.text.toString()
                val nome = editTextCadastroNome.text.toString()
                val username = editTextCadastroUsername.text.toString()
                val endereco = editTexCadastroEndereco.text.toString()
                val codigoPostal = editTextTextPostalAddress.text.toString()
                val dataNascimento = editTextCadastroDataNascimento.text.toString()

                registerViewModel.saveRegister(email, password, nome,
                    username, endereco, codigoPostal, dataNascimento)
            }
            else {
                registerViewModel.changeMessage("Senhas não conferem")
            }
        }

        imageViewUserRegister.setOnClickListener {
            selectPicture()
        }
    }

    private fun selectPicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest
                .permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 1)
        } else{
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val picture: Bitmap = data!!.getParcelableExtra("data")!!
            imageViewUserRegister.setImageBitmap(picture)
            registerViewModel.takePicture(picture)
        } else {
            Snackbar.make(requireContext(), this.requireView(), "Não deu " +
                    "certo", Snackbar.LENGTH_LONG).show()
        }
    }
}