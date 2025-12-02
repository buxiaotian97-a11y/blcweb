
package com.example.blcweb.service;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.blcweb.dto.ResultbgRow;
import com.example.blcweb.dto.QuestionRow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

@Service
public class SheetsService {

    private static final String APPLICATION_NAME = "BlackCompanySimulator";

    private static final String SHEET_ID = "1e8v9mH8wLLH0FKWUOZCNA2KTa9r7GdUVyuzerCzhTII";

    private final Sheets sheets;

    // ★ コンストラクタで Google Sheets API クライアントを作る
    public SheetsService() throws GeneralSecurityException, IOException {

        // resourcesフォルダ直下に置いた credentials.json を読む
        InputStream in = getClass().getResourceAsStream("/credentials.json");
        if (in == null) {
            this.sheets = null;
            return;
        }

        GoogleCredential credential = GoogleCredential
                .fromStream(in)
                .createScoped(List.of(SheetsScopes.SPREADSHEETS_READONLY));

        this.sheets = new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                credential
        )
        .setApplicationName(APPLICATION_NAME)
        .build();
    }

    /** questions シートの内容を QuestionRow のリストとして読み込む */
    public List<QuestionRow> readQuestions() {
    	
        if (this.sheets == null) {
            System.out.println("⚠ Sheets 連携オフのため、質問は読み込まず空リストを返します");
            return List.of(); 
        }
    	
        try {
            ValueRange vr = sheets.spreadsheets().values()
                    .get(SHEET_ID, "questions!A:I")
                    .execute();

            List<QuestionRow> list = new ArrayList<>();
            var values = vr.getValues();
            if (values == null || values.size() <= 1) {
                return list; // データなし
            }

            for (List<Object> row : values.subList(1, values.size())) {

                String code      = getCell(row, 0);
                String qtext     = getCell(row, 1);
                String category  = getCell(row, 2);
                int yesPoint     = parseInt(getCell(row, 3));
                int noPoint      = parseInt(getCell(row, 4));

                // ★ ここはそのまま文字列で保持
                String nextYesStr = getCell(row, 5);
                String nextNoStr  = getCell(row, 6); 
                
                boolean isStart  = parseBoolean(getCell(row, 7));
                boolean finish   = parseBoolean(getCell(row, 8));

                list.add(new QuestionRow(
                        code,
                        qtext,
                        category,
                        yesPoint,
                        noPoint,
                        nextYesStr,
                        nextNoStr,
                        isStart,
                        finish
                ));
            }

            return list;

        } catch (IOException e) {
            throw new RuntimeException("Failed to read questions from Sheets", e);
        }
    }
    public List<ResultbgRow> readBackgrounds() {
        if (this.sheets == null) {
            System.out.println("⚠ Sheets 連携オフのため、背景は読み込まず空リストを返します");
            return List.of();
        }

        try {
            // ★ シート名と範囲はあなたのシートに合わせて変更してOK
            ValueRange vr = sheets.spreadsheets().values()
                    .get(SHEET_ID, "'resultimage'!A:E")
                    .execute();

            List<ResultbgRow> list = new ArrayList<>();
            var values = vr.getValues();
            if (values == null || values.size() <= 1) {
                return list;
            }

            // 1行目はヘッダー想定なので subList(1, ...) で2行目から読む
            for (List<Object> row : values.subList(1, values.size())) {
                String minStr   = getCell(row, 0); // A
                String maxStr   = getCell(row, 1); // B
                String mode     = getCell(row, 2); // C
                String cssClass = getCell(row, 3); // D
                String bgUrl = getCell(row, 4);

                int minScore = parseInt(minStr);
                int maxScore = parseInt(maxStr);
                if (mode == null || mode.isBlank()) {
                    mode = "ANY"; // 未指定ならANY扱い
                }

                list.add(new ResultbgRow(
                        minScore,
                        maxScore,
                        mode,
                        cssClass,
                        bgUrl
                ));
            }

            return list;

        } catch (IOException e) {
            throw new RuntimeException("Failed to read backgrounds from Sheets", e);
        }
    }

    private String getCell(List<Object> row, int index) {
        if (index >= row.size()) return "";
        Object v = row.get(index);
        return (v == null) ? "" : v.toString();
    }

    private int parseInt(String s) {
        if (s == null || s.isBlank()) return 0;
        return Integer.parseInt(s);
    }

    private boolean parseBoolean(String s) {
        if (s == null || s.isBlank()) return false;
        return Boolean.parseBoolean(s);
    }

}
