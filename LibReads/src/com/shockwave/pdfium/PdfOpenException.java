package com.shockwave.pdfium;

import java.io.IOException;

public class PdfOpenException extends IOException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PdfOpenException(String detailMessage) {
        super(detailMessage);
    }

}
