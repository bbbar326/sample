package science.bbbar326.csvsave;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App4 {
	public static void main(String[] args) throws Exception {
		System.out.println("App4 main処理実行開始");
		PrintWriter p = null;

		try {

			// -----------------------------------------------
			// ファイルパスを取得（第一引数があればそれを利用）
			// -----------------------------------------------
			String filePath = "";
			if (args != null && args.length != 0) {
				filePath = args[0];
			} else {
				filePath = "/temp/App4_main_result.csv";
			}
			System.out.println("出力先：" + filePath);

			HttpClientContext context = HttpClientContext.create();
			CookieStore cookieStore = new BasicCookieStore();
			context.setCookieStore(cookieStore);

			// -----------------------------------------------
			// CSV書込み用Writerを取得
			// -----------------------------------------------
			p = new PrintWriter(new BufferedWriter(new FileWriter(filePath, false)));

			try (
					CloseableHttpClient httpClient = HttpClients.createDefault();) {

				// -----------------------------------------------
				// CSRFトークン取得用に一度アクセスする
				// -----------------------------------------------
				HttpGet get1 = new HttpGet("https://premier.no1s.biz/");

				try (
						CloseableHttpResponse response = httpClient.execute(get1, context);) {
				}

				// -----------------------------------------------
				// ログイン
				// -----------------------------------------------
				HttpPost post1 = new HttpPost("https://premier.no1s.biz/users/login");
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("_method", "POST"));
				params.add(new BasicNameValuePair("_csrfToken", getCookieValue(cookieStore, "csrfToken")));
				params.add(new BasicNameValuePair("email", "micky.mouse@no1s.biz"));
				params.add(new BasicNameValuePair("password", "micky"));
				UrlEncodedFormEntity paramEntity = new UrlEncodedFormEntity(params);
				post1.setEntity(paramEntity);

				String redirectUrl = "";

				try (
						CloseableHttpResponse response = httpClient.execute(post1, context);) {
				}

				int count = 1;

				// -----------------------------------------------
				// page=*が取得できなくなるまでGET
				// -----------------------------------------------
				while (true) {

					redirectUrl = "https://premier.no1s.biz/admin?page=" + count;

					// -----------------------------------------------
					// リダイレクトURLにアクセスしてCSVに出力するための情報を取得する
					// -----------------------------------------------
					HttpGet get2 = new HttpGet(redirectUrl);
					//			System.out.println(redirectUrl);
					try (
							CloseableHttpResponse response = httpClient.execute(get2, context);) {

						if (response.getStatusLine().getStatusCode() != 200) {
							break;
						}

						Document document = Jsoup.parse(readResponse(response));

						// -----------------------------------------------
						// tr要素を検索
						// -----------------------------------------------
						Elements elements = document.select("tr");

						for (Element element : elements) {
							StringBuilder sb = new StringBuilder();
							for (Element element2 : element.children()) {
								if ("th".equals(element2.nodeName())) {
									// -----------------------------------------------
									// thはスキップ
									// -----------------------------------------------
									continue;
								}

								sb.append("\"");
								sb.append(element2.text());
								sb.append("\",");
							}
							if (sb.length() != 0) {
								// -----------------------------------------------
								// 出力
								// -----------------------------------------------
								//							System.out.println(sb);
								p.write(sb.toString());
								p.write("\n");
							}
						}
					}
					count++;
				}
			}
			System.out.println("App4 main処理実行終了：正常終了");
		} catch (Exception e) {
			System.out.println("App4 main処理実行終了：異常終了");
			throw e;
		} finally {
			if (p != null) {
				p.flush();
				p.close();
			}
		}
	}

	public static String getCookieValue(CookieStore cookieStore, String cookieName) {
		String value = null;
		for (Cookie cookie : cookieStore.getCookies()) {
			if (cookie.getName().equals(cookieName)) {
				value = cookie.getValue();
				break;
			}
		}
		return value;
	}

	public static String readResponse(CloseableHttpResponse response)
			throws Exception {
		BufferedReader reader = null;
		String content = "";
		String line = null;
		HttpEntity entity = response.getEntity();

		reader = new BufferedReader(new InputStreamReader(entity.getContent()));
		while ((line = reader.readLine()) != null) {
			content += line;
		}
		EntityUtils.consume(entity);
		if (reader != null) {
			reader.close();
		}
		return content;
	}

}
