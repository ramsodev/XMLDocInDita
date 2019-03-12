package net.ramso.tools.graph;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.canvas.mxSvgCanvas;
import com.mxgraph.layout.mxStackLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxCellRenderer.CanvasFactory;
import com.mxgraph.util.mxDomUtils;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;

import net.ramso.doc.dita.tools.DitaConstants;
import net.ramso.doc.dita.xml.Config;
import net.ramso.tools.FileTools;

public abstract class AbstractGraph {
	protected static String SUFFIX = "";
	private String fileName;
	private mxGraph graph;

	protected void export(mxGraph graph) throws IOException {
		final String filename = Config.getOutputDir() + File.separator + getFileName();
		if (FileTools.checkPath(filename, true)) {
			final mxSvgCanvas canvas = (mxSvgCanvas) mxCellRenderer.drawCells(graph, null, 1, null,
					new CanvasFactory() {
						@Override
						public mxICanvas createCanvas(int width, int height) {
							final CustomSvgCanvas canvas = new CustomSvgCanvas(
									mxDomUtils.createSvgDocument(width, height));
							canvas.setEmbedded(true);
							return canvas;
						}
					});
			mxUtils.writeFile(mxXmlUtils.getXml(canvas.getDocument()), filename);
		}
	}

	public abstract String generate();

	public String getFileName() {
		return fileName;
	}

	/**
	 * @return the graph
	 */
	protected mxGraph getGraph() {
		return graph;
	}

	protected mxCell insertIcon(mxCell parent, String icon, int size) {
		return (mxCell) getGraph().insertVertex(parent, GraphConstants.EXCLUDE_PREFIX_ICON + parent.getId(), "", 0, 0,
				size, size, GraphTools.getStyleImage(true, size - 2, size - 2, icon));
	}

	protected void morphGraph(mxGraph graph, mxGraphComponent graphComponent) {
		final mxStackLayout layout = new mxStackLayout(graph, true, 50);

		// mxPartitionLayout layout = new mxPartitionLayout(graph,true, 50, 100);
		layout.execute(graph.getDefaultParent());
	}

	protected void process(mxGraph graph) {
		final JFrame f = new JFrame();
		f.setSize(800, 800);
		f.setLocation(300, 200);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final mxGraphComponent graphComponent = new mxGraphComponent(graph);
		f.getContentPane().add(graphComponent, BorderLayout.CENTER);
		f.setVisible(false);
		morphGraph(graph, graphComponent);
		try {
			export(graph);
		} catch (final Exception e) {
			net.ramso.tools.LogManager.warn("Error al exportar el diagrama " + getFileName(), e);
		}
	}

	protected void resizeCell(mxCell cell, int maxWith) {
		if (!cell.getId().startsWith(GraphConstants.EXCLUDE_PREFIX_ICON)) {
			final mxGeometry g = cell.getGeometry();
			if (cell.getId().endsWith(DitaConstants.SUFFIX_ADDRESS)) {
				maxWith -= 20;
			}
			g.setWidth(maxWith);
			final int e = cell.getChildCount();
			if (!cell.getId().startsWith(GraphConstants.EXCLUDE_PREFIX_GROUP)) {
				for (int i = 0; i < e; i++) {
					if (!cell.getId().startsWith(GraphConstants.EXCLUDE_PREFIX_GROUP)) {
						resizeCell((mxCell) cell.getChildAt(i), maxWith);
					} else {

					}
				}
			}
		}
	}

	public boolean scale() {
		if (getGraph().getGraphBounds().getWidth() > 500)
			return true;
		return false;
	}

	protected void setFileName(String file_name) {
		fileName = (GraphConstants.IMAGE_PATH + File.separator + file_name + SUFFIX + "."
				+ GraphConstants.SVG_EXTENSION).replaceAll("\\s+", "_");
	}

	protected void setGraph(mxGraph graph) {
		this.graph = graph;
	}
}
