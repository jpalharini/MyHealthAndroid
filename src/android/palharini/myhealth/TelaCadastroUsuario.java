package android.palharini.myhealth;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.palharini.myhealth.daos.PreferenciasDAO;
import android.palharini.myhealth.daos.UsuarioDAO;
import android.palharini.myhealth.entidades.Preferencias;
import android.palharini.myhealth.entidades.Usuario;
import android.palharini.myhealth.fragmentos.FragmentoDatePicker;
import android.palharini.myhealth.sessao.GerenciamentoSessao;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class TelaCadastroUsuario extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		setContentView(R.layout.activity_tela_cadastro_usuario);
					
		// Campos de entrada
		final EditText email = (EditText) findViewById(R.id.editEmail);
		final EditText senha = (EditText) findViewById(R.id.editSenha);
		final EditText confSenha = (EditText) findViewById(R.id.editConfSenha);
		final EditText nome = (EditText) findViewById(R.id.editNome);
		final EditText dataNasc = (EditText) findViewById(R.id.editNasc);
		final EditText altura = (EditText) findViewById(R.id.editAltura);
		final EditText peso = (EditText) findViewById(R.id.editPeso);
		
		final CheckBox checkLembretePeso = (CheckBox) findViewById(R.id.checkLembretePeso);
		final CheckBox checkAlvoBPM = (CheckBox) findViewById(R.id.checkAlvoBPM);
		
		Button okButton = (Button) findViewById(R.id.okButton);
		
		final GerenciamentoSessao sessao = new GerenciamentoSessao(getApplicationContext());
		
		dataNasc.setOnClickListener(new EditText.OnClickListener () {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new FragmentoDatePicker((EditText) v).show(getFragmentManager(), "datePicker");
			}
			
		});
		
		okButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick (View v){
				
				String emailString = email.getText().toString();
				String nomeString = nome.getText().toString();
				Double alturaDouble = Double.parseDouble(altura.getText().toString());
				Double pesoDouble = Double.parseDouble(peso.getText().toString());

				String senhaString = senha.getText().toString();
				String confSenhaString = confSenha.getText().toString();
				String criptSenha = null;
				
				if (senhaString.equals(confSenhaString)){
					try {
						MessageDigest md = MessageDigest.getInstance("MD5");
						md.update(senhaString.getBytes("UTF-8"));
						BigInteger hash = new BigInteger(1, md.digest());
						criptSenha = hash.toString(16);
					} catch (NoSuchAlgorithmException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					String dataNascString = dataNasc.getText().toString();
					final String formatoData = "d/MM/yyyy";
					final String formatoDataSQL = "yyyy-MM-dd";
					SimpleDateFormat sdf = new SimpleDateFormat(formatoData);
					SimpleDateFormat sdfSQL = new SimpleDateFormat(formatoDataSQL);
					Date dataNascDate = null;
					try {
						dataNascDate = sdf.parse(dataNascString);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
							e.printStackTrace();
						}
						String dataNascSQL = sdfSQL.format(dataNascDate);
						
						UsuarioDAO usrdao = new UsuarioDAO();
						usrdao.cadastrarUsuario(new Usuario(
								0, 
								emailString,
								criptSenha,
								nomeString, 
								dataNascSQL,
								alturaDouble,
								pesoDouble
								));
		                
						Usuario usuario = usrdao.buscarUsuarioEmail(emailString);
		                sessao.criarSessao(usuario.getId(), usuario.getNome(), usuario.getEmail());
		                
		                PreferenciasDAO prefsdao = new PreferenciasDAO();
						prefsdao.cadastrarPreferencias(new Preferencias(
								sessao.getIdUsuario(),
								checkLembretePeso.isChecked(),
								checkAlvoBPM.isChecked()
								));
				}
				
				Intent irTelaPrincipal = new Intent(getApplicationContext(), TelaAcompanhamento.class);
				startActivity(irTelaPrincipal);
				finish();
			}
		});
	}
}