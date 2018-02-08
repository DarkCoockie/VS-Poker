package de.hsos.vs;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author heikerli
 */
@WebServlet(urlPatterns = {"/BillBoardServer"})
public class BillBoardServer extends HttpServlet {
    private final BillBoard bb = new BillBoard ("BillBoardServer");
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doGet");
        String caller_ip = request.getRemoteAddr();
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String table = bb.readContents(caller_ip);
        try {
            out.println(table);
        } finally {
            out.close();
        }
    }
      
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doPost");
        
        String text = request.getParameter("text");
        System.out.println(text);
        String poster_ip = request.getRemoteAddr();
        
        bb.createEntry(text, poster_ip);
        String caller_ip = request.getRemoteAddr();
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String table = bb.readContents(caller_ip);
        try {
            out.println(table);
        } finally {
            out.close();
        }
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doDelete");
        String poster_ip = request.getRemoteAddr();
        int id= Integer.parseInt(request.getParameter("id"));
        bb.deleteEntry(id, poster_ip);
    }
    /*
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doPut");
        String poster_ip = request.getRemoteAddr();
        String text = request.getParameter("text");
        String id = request.getParameter("id");
        System.out.println(text + " " + id);
        bb.updateEntry(Integer.parseInt(id), text, poster_ip);
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doDelete");
        String poster_ip = request.getRemoteAddr();
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = request.getReader();
            reader.mark(10000);

            String line;
            do {
                line = reader.readLine();
                if (line != null)
                    sb.append(line).append("\n");
            } while (line != null);
            reader.close();
        } catch (IOException e) {
            System.out.println("getPostData couldn't.. get the post data");
        }
        String data = sb.toString();
        System.out.println("data:"+data);
        String[] pvlist = data.split("=");
        int id = Integer.parseInt(pvlist[1].trim());
        bb.deleteEntry(id, poster_ip);
    }
    */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doPut");
        String poster_ip = request.getRemoteAddr();
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = request.getReader();
            reader.mark(10000);

            String line;
            do {
                line = reader.readLine();
                if (line != null)
                    sb.append(line).append("\n");
            } while (line != null);
            reader.close();
            // do NOT close the reader here, or you won't be able to get the
            // post data twice
        } catch (IOException e) {
            System.out.println("getPostData couldn't.. get the post data"); 
        }
        String data = sb.toString();
        String[] pvlist = data.split("&");
        String[] pvtext = pvlist[0].split("=");
        String[] pvid = pvlist[1].split("=");
        String text = pvtext[1].trim();
        int id = Integer.parseInt(pvid[1].trim());
        bb.updateEntry(id, text, poster_ip);
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
