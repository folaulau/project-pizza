package com.lovemesomecoding.pizzaria.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class HttpRequestInterceptor implements ClientHttpRequestInterceptor {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		logRequest(request, body);
		ClientHttpResponse response = execution.execute(request, body);
		logResponse(response);
		return response;
	}

	private void logRequest(HttpRequest request, byte[] body) throws IOException {
		if (log.isInfoEnabled()) {
			try {
				System.out.println();
				System.out.println(
						"===========================Http Request begin=============================================");
				System.out.println("URI         :" + request.getURI());
				System.out.println("Method      :" + request.getMethod());
				System.out.println("Headers     :" + request.getHeaders());
				System.out.println("Request body:" + new String(body, "UTF-8"));
				System.out.println(
						"===========================Http Request end===============================================");
				System.out.println();
			} catch (Exception e) {
				log.warn("Exception msg: {}", e.getMessage());
			}

		}
	}

	private String logResponse(ClientHttpResponse response) throws IOException {
		StringBuilder result = new StringBuilder();

		if (log.isInfoEnabled()) {

			try {
				System.out.println();
				System.out.println(
						"===========================Http Response begin============================================");
				System.out.println("Status code  :" + response.getStatusCode());
				System.out.println("Status code value  :" + response.getStatusCode().value());
				System.out.println("Headers      :" + response.getHeaders());

			} catch (Exception e) {
				log.warn("Exception msg: {}", e.getMessage());
			}

			try {
				InputStream inputStream = response.getBody();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				StringBuilder out = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					out.append(line);
				}
				reader.close();

				System.out.println("Response body: " + out.toString());
				System.out.println(
						"===========================Http Response end==============================================");
				System.out.println();

				result.append(out.toString());
			} catch (Exception e) {
				log.warn("Exception msg: {}", e.getMessage());
				result.append(e.getLocalizedMessage());
			}

		}
		return result.toString();
	}

}
