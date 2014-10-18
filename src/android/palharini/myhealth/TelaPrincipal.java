package android.palharini.myhealth;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.palharini.myhealth.daos.UsuarioDAO;
import android.palharini.myhealth.entidades.Usuario;
import android.palharini.myhealth.sessao.GerenciamentoSessao;
import android.widget.Button;
import android.widget.TextView;

public class TelaPrincipal extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tela_principal);
		
		final TextView ola = (TextView) findViewById(R.id.textOla);
		final TextView imc = (TextView) findViewById(R.id.IMC);
		final TextView imcStatus = (TextView) findViewById(R.id.textStatusIMC);
		
		final Button buttonAcompanhamento = (Button) findViewById(R.id.buttonAcompanhamento);
		final Button buttonDados = (Button) findViewById(R.id.buttonDados);
		final Button buttonConfiguracoes = (Button) findViewById(R.id.buttonConfiguracoes);
		
		GerenciamentoSessao sessao = new GerenciamentoSessao(getApplicationContext());
		
		UsuarioDAO dao = new UsuarioDAO();
		Usuario usuario = dao.buscarUsuario(sessao.getIdUsuario());
		
		String nomeUsuario = usuario.getNome();
		Double pesoUsuario = usuario.getPeso();
		Double alturaUsuario = usuario.getAltura();
		
		String primeiroNomeUsuario;
		if (nomeUsuario.contains(" ")) {
			primeiroNomeUsuario = nomeUsuario.substring(0, nomeUsuario.indexOf(" "));
		}
		else {
			primeiroNomeUsuario = nomeUsuario;
		}
		ola.setText(getString(R.string.textOla) + primeiroNomeUsuario);
		
		Double imcDouble = (pesoUsuario / (alturaUsuario * alturaUsuario));
		imc.setText(imcDouble.toString());
		
		String[] listaFaixasIMC = getResources().getStringArray(R.array.faixasIMC);
		final List<String> faixas = Arrays.asList(listaFaixasIMC);
		
		if (imcDouble > 0 && imcDouble <= 18.5)
			imcStatus.setText(faixas.get(0));
		if (imcDouble >= 18.6 && imcDouble <= 24.9)
			imcStatus.setText(faixas.get(1));
		if (imcDouble >= 25 && imcDouble <= 29.9)
			imcStatus.setText(faixas.get(2));
		if (imcDouble >= 30 && imcDouble <= 34.9)
			imcStatus.setText(faixas.get(3));
		if (imcDouble >= 35 && imcDouble <= 39.9)
			imcStatus.setText(faixas.get(4));
		if (imcDouble >= 40)
			imcStatus.setText(faixas.get(5));
		
	}
}
