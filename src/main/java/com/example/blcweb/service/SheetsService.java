
package com.example.blcweb.service;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.example.blcweb.dto.QuestionRow;
import com.example.blcweb.dto.QuestionbgRow;
import com.example.blcweb.dto.ResultbgRow;
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

    public SheetsService() throws GeneralSecurityException, IOException {
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

    private <T> List<T> readSheet(
            String range,
            Function<List<Object>, T> rowMapper
    ) {

        if (this.sheets == null) {
            System.out.println("⚠ Sheets 連携オフのため、" + range + " は読み込まず空リストを返します");
            return List.of();
        }

        try {
            ValueRange vr = sheets.spreadsheets().values()
                    .get(SHEET_ID, range)
                    .execute();

            var values = vr.getValues();
            List<T> list = new ArrayList<>();

            if (values == null || values.size() <= 1) {
                return list;
            }

            for (List<Object> row : values.subList(1, values.size())) {
                T dto = rowMapper.apply(row);
                if (dto != null) {
                    list.add(dto);
                }
            }

            return list;

        } catch (Exception e) {
            System.err.println("⚠ Sheets 読み込み失敗 (" + range + ") : " + e.getMessage());
            return List.of();
        }
    }

    public List<QuestionRow> readQuestions() {
        return readSheet("questions!A:I", row -> {
            String code      = getCell(row, 0);
            String qtext     = getCell(row, 1);
            String category  = getCell(row, 2);
            int yesPoint     = parseInt(getCell(row, 3));
            int noPoint      = parseInt(getCell(row, 4));

            String nextYesStr = getCell(row, 5);
            String nextNoStr  = getCell(row, 6);

            boolean isStart  = parseBoolean(getCell(row, 7));
            boolean finish   = parseBoolean(getCell(row, 8));

            return new QuestionRow(
                    code,
                    qtext,
                    category,
                    yesPoint,
                    noPoint,
                    nextYesStr,
                    nextNoStr,
                    isStart,
                    finish
            );
        });
    }

    public List<ResultbgRow> readBackgrounds() {
        return readSheet("'resultimage'!A:E", row -> {
            String minStr   = getCell(row, 0);
            String maxStr   = getCell(row, 1);
            String mode     = getCell(row, 2);
            String cssClass = getCell(row, 3);
            String bgUrl    = getCell(row, 4);

            int minScore = parseInt(minStr);
            int maxScore = parseInt(maxStr);
            if (mode == null || mode.isBlank()) {
                mode = "ANY";
            }

            return new ResultbgRow(
                    minScore,
                    maxScore,
                    mode,
                    cssClass,
                    bgUrl
            );
        });
    }

    public List<QuestionbgRow> readQuestionBackgrounds() {
        return readSheet("'questionimage'!A:E", row -> {
            String type         = getCell(row, 0);
            String questionCode = getCell(row, 1);
            String minStr       = getCell(row, 2);
            String mode         = getCell(row, 3);
            String bgUrl        = getCell(row, 4);

            int minScore = parseInt(minStr);
            if (mode == null || mode.isBlank()) {
                mode = "ANY";
            }

            return new QuestionbgRow(
                    type,
                    questionCode,
                    minScore,
                    mode,
                    bgUrl
            );
        });
    }

    private String getCell(List<Object> row, int index) {
        if (index >= row.size()) return null;
        Object v = row.get(index);
        if (v == null) return null;

        String s = v.toString().trim();
        return s.isEmpty() ? null : s;
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
