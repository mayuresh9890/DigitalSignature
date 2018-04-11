using System.IO;
using System.Linq;
using SautinSoft.Document;
using SautinSoft.Document.Drawing;
namespace Sample
{
    class Sample
    {
        static void Main(string[] args)
        {
            DigitalSignature();
        }

        /// Load an existing document (*.docx, *.rtf, *.pdf, *.html, *.txt, *.pdf) and save it in a PDF document with the digital signature.
        public static void DigitalSignature()
        {
            // Path to a loadable document.
            string loadPath = @"C:\Users\admin\Desktop\Test\Test\Test.pdf";

            DocumentCore dc = DocumentCore.Load(loadPath);
            
            // Signature line added with MS Word -> Insert tab -> Signature Line button by default has description 'Microsoft Office Signature Line...'.
            ShapeBase signatureLine = dc.GetChildElements(true).OfType<ShapeBase>().FirstOrDefault();
            
            // This picture symbolizes a handwritten signature
            Picture signature = new Picture(dc, "C:\\Users\\admin\\Desktop\\Test\\Test\\signature.png");

            // Signature in this document will be 4.5 cm right of TopLeft position of signature line
            // and 4.5 cm below of TopLeft position of signature line.
            signature.Layout = Layout.Floating(
               new HorizontalPosition(4.5, LengthUnit.Centimeter, HorizontalPositionAnchor.Page),
               new VerticalPosition(-4.5, LengthUnit.Centimeter, VerticalPositionAnchor.Page),
               signature.Layout.Size);

            dc.Sections.Last().Blocks.Add(
                new Paragraph(dc, signature));

            //signature.Layout = Layout.Inline(signature.Layout.Size);
            PdfSaveOptions options = new PdfSaveOptions();

            // Path to the certificate (*.pfx).
            options.DigitalSignature.CertificatePath = "C:\\Users\\admin\\Desktop\\Test\\Test\\xyz.pfx";

            // Password of the certificate.
            options.DigitalSignature.CertificatePassword = "123456";

            // Additional information about the certificate.
            options.DigitalSignature.Location = "World Wide Web";
            options.DigitalSignature.Reason = "Document.Net by SautiSoft";
            options.DigitalSignature.ContactInfo = "info@sautinsoft.com";

            // Placeholder where signature should be visualized.
            options.DigitalSignature.SignatureLine = signatureLine;

            // Visual representation of digital signature.
            options.DigitalSignature.Signature = signature;

            string savePath = Path.ChangeExtension(loadPath, ".pdf");
            dc.Save(savePath, options);
            ShowResult(savePath);
        }

        static void ShowResult(string filename)
        {
            System.Diagnostics.ProcessStartInfo psi = new System.Diagnostics.ProcessStartInfo(filename) { UseShellExecute = true };
            System.Diagnostics.Process.Start(psi);
        }
    }
}