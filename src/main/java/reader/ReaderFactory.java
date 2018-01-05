package reader;

public class ReaderFactory {

    public XMLReader createXMLReader(String type) {
        switch (type) {

            // ����
            case "���°���":
                return new XMLReaderCri();

            // ����
            case "���°���":
                return new XMLReaderCiAndAd();

            // ����
            case "��������":
                return new XMLReaderCiAndAd();

            default:
                break;
        }
        return null;
    }
}