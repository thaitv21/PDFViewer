package com.github.barteksc.pdfviewer;

import android.content.Context;
import android.os.AsyncTask;

import com.github.barteksc.pdfviewer.source.DocumentSource;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

public class DecodeAsyncTask extends AsyncTask<Void, Void, Throwable> {

    public interface Callback {
        void onComplete(PdfDocument pdfDocument);
        void onError(Throwable t);
    }

    private boolean cancelled;

    private PDFView pdfView;

    private Context context;
    private PdfiumCore pdfiumCore;
    private PdfDocument pdfDocument;
    private String password;
    private DocumentSource docSource;
    private Callback callback;

    public DecodeAsyncTask(DocumentSource docSource, PDFView pdfView, Callback callback) {
        this.cancelled = false;
        this.pdfView = pdfView;
        this.password = null;
        this.pdfiumCore = new PdfiumCore(pdfView.getContext());
        context = pdfView.getContext();
        this.callback = callback;
    }

    public DecodeAsyncTask(DocumentSource docSource, String password, PDFView pdfView, PdfiumCore pdfiumCore, Callback callback) {
        this.docSource = docSource;
        this.cancelled = false;
        this.pdfView = pdfView;
        this.password = password;
        this.pdfiumCore = pdfiumCore;
        context = pdfView.getContext();
        this.callback = callback;
    }

    @Override
    protected Throwable doInBackground(Void... params) {
        try {
            pdfDocument = docSource.createDocument(context, pdfiumCore, password);
            return null;
        } catch (Throwable t) {
            return t;
        }
    }

    @Override
    protected void onPostExecute(Throwable t) {
        if (t != null) {
            callback.onError(t);
            return;
        }
        if (!cancelled) {
            callback.onComplete(pdfDocument);
        }
    }

    @Override
    protected void onCancelled() {
        cancelled = true;
    }
}
