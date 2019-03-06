package net.ramso.doc.dita.xml.schema.model.graph;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxGraph;

import net.ramso.doc.dita.tools.Constants;
import net.ramso.doc.dita.xml.schema.model.AttributeGroupModel;
import net.ramso.doc.dita.xml.schema.model.AttributeModel;
import net.ramso.doc.dita.xml.schema.model.ComplexTypeModel;
import net.ramso.doc.dita.xml.schema.model.ElementModel;
import net.ramso.doc.dita.xml.schema.model.GroupModel;
import net.ramso.doc.dita.xml.schema.model.IComplexContentModel;
import net.ramso.tools.graph.GraphConstants;
import net.ramso.tools.graph.GraphTools;

public class ComplexTypeGraph extends AbstractXmlGraph {

	private ComplexTypeModel complexType;
	private int iconsColumn = 0;
	private List<mxCell> cellTypes;
	private int contentPosition = 0;
	private mxCell typeGroup;
	private boolean addType = false;

	public ComplexTypeGraph(ComplexTypeModel complexType) {
		super();
		this.complexType = complexType;
		SUFFIX = Constants.SUFFIX_COMPLEXTYPE;
		setFileName(complexType.getName());
	}

	public ComplexTypeGraph(ComplexTypeModel complexType, mxGraph graph) {
		this(complexType);
		setGraph(graph);
	}

	@Override
	public String generate() {
		this.addType = true;
		setGraph(new mxGraph());
		getGraph().setAutoSizeCells(true);
		getGraph().setCellsResizable(true);
		mxCell parent = (mxCell) getGraph().getDefaultParent();
		mxCell complexTypeCell = createComplexTypeCell(parent);
		getGraph().addCell(complexTypeCell);
		if (isAddType()) {
			getGraph().addCell(typeGroup);
		}
		process(getGraph());
		return getFileName();
	}

	public mxCell createComplexTypeCell(mxCell parent) {
		return createComplexTypeCell(parent, complexType.getName());
	}

	public mxCell createComplexTypeCell(mxCell parent, String name) {
		return createComplexTypeCell(parent, name, 0, 0);
	}

	public mxCell createComplexTypeCell(mxCell parent, String name, int x, int y) {
		Rectangle2D base = GraphTools.getTextSize(name);
		int height = (int) (base.getHeight() + (base.getHeight() / 2));

		int[] sizes = getSizes();
		int width = sizes[0] + 100 + sizes[1] + (height + (height / 2));
		return createComplexTypeCell(parent, name, x, y, width, height, sizes);
	}

	public mxCell createComplexTypeCell(mxCell parent, int x, int y, int width, int height) {
		int[] sizes = getSizes();
		return createComplexTypeCell(parent, complexType.getName(), x, y, width, height, sizes);
	}

	public mxCell createComplexTypeCell(mxCell parent, int x, int y, int width, int height, int[] sizes) {
		return createComplexTypeCell(parent, complexType.getName(), x, y, width, height, sizes);
	}

	public mxCell createComplexTypeCell(mxCell parent, String name, int x, int y, int width, int height, int[] sizes) {
		String color = "BLUE";
		if (name.startsWith("("))
			color = "LIGHTGRAY";
		mxCell cell = (mxCell) getGraph().createVertex(parent, name + Constants.SUFFIX_COMPLEXTYPE, "", x, y, width,
				height, GraphTools.getStyle(false, true));
		mxCell titulo = (mxCell) getGraph().insertVertex(cell, "Title" + name + Constants.SUFFIX_COMPLEXTYPE, name, 0,
				0, width, height, GraphTools.getStyle(true, true, color, height));
		super.insertIcon((mxCell) titulo, Constants.SUFFIX_COMPLEXTYPE.toLowerCase(), height);
		y += height;
		width -= 6;
		if (complexType.getElements().size() > 0 || complexType.getAttributes().size() > 0) {
			mxCell subCell = (mxCell) getGraph().insertVertex(cell,
					complexType.getName() + Constants.SUFFIX_COMPLEXTYPE, "", x + 3, y, width, height,
					GraphTools.getStyle(false, false));

			contentPosition = 0;
			typeGroup = (mxCell) getGraph().createVertex(parent,
					GraphConstants.EXCLUDE_PREFIX_GROUP + Constants.SUFFIX_TYPE, "", 100, 100, 300, 0,
					mxConstants.STYLE_AUTOSIZE + "=1;" + mxConstants.STYLE_RESIZABLE + "=1;"
							+ mxConstants.STYLE_STROKE_OPACITY + "=0;" + mxConstants.STYLE_FILL_OPACITY + "=0;");
			apppendContentAttributeGroup(subCell, null, complexType.getAttributeGroups(), sizes, height);
			apppendContent(subCell, null, complexType.getAttributes(), sizes, height);
			apppendContent(subCell, null, complexType.getElements(), sizes, height);
			width = (int) resize(subCell, sizes);
		}
		cell.getGeometry().setWidth(width);
		titulo.getGeometry().setWidth(width);
		return cell;
	}

	private double resize(mxCell cell, int[] sizes) {
		List<Integer> icons = new ArrayList<Integer>();
		double heigth = 0;
		double width = 0;
		double iWidth = 0;
		for (int i = 0; i < cell.getChildCount(); i++) {
			mxCell child = (mxCell) cell.getChildAt(i);
			if (child.getId().startsWith(GraphConstants.EXCLUDE_PREFIX_ICON)) {
				if (icons.size() == 0) {
					iWidth = child.getGeometry().getWidth();
				}
				icons.add(i);
			}
		}
		double x = (iWidth * icons.size()) + ((iWidth / 3) * icons.size()) + (iWidth / 3);
		width = sizes[0] + 100 - 6 + sizes[1] + x;
		for (int i = 0; i < cell.getChildCount(); i++) {
			mxCell child = (mxCell) cell.getChildAt(i);
			if (!child.getId().startsWith(GraphConstants.EXCLUDE_PREFIX_ICON)) {
				child.getGeometry().setX(x);
				heigth += child.getGeometry().getHeight();
			}
		}
		cell.getGeometry().setWidth(width);
		cell.getGeometry().setHeight(heigth);
		x = cell.getGeometry().getCenterX();
		double y = (heigth / 2) - (iWidth / 2);
		int j = 0;
		for (Integer i : icons) {
			mxCell child = (mxCell) cell.getChildAt(i);
			if (j == 0) {

				x = (iWidth / 3);
				child.getGeometry().setY(y);
				child.getGeometry().setX(x);
				x += (iWidth + (iWidth / 3));
			} else {
				child.getGeometry().setX(x);
				x += (iWidth + (iWidth / 3));
			}
			j++;
		}

		return width;

	}

	private void apppendContent(mxCell parent, mxCell iconParent, ArrayList<IComplexContentModel> elements,
			int[] widths, int height) {
		int x = height + (height / 2);
		for (IComplexContentModel element : elements) {
			if (element.isElement()) {
				if (element instanceof ElementModel) {
					ElementModel ele = (ElementModel) element;
					ElementGraph eg = new ElementGraph(ele, getGraph());
					mxCell cellLine = eg.createElementLine(parent, x, getContentPosition(), widths[0], height,
							widths[1]);
					mxCell cellType = inserType(ele);
					if (iconParent != null) {
						getGraph().insertEdge(getGraph().getDefaultParent(), "", "", iconParent, cellLine,
								mxConstants.STYLE_EDGE + "=" + mxConstants.EDGESTYLE_ORTHOGONAL + ";");
					}
					if (isAddType()) {
						addCellType(cellType);
						getGraph().insertEdge(getGraph().getDefaultParent(), "", "", cellLine, cellType,
								mxConstants.STYLE_EDGE + "=" + mxConstants.EDGESTYLE_ORTHOGONAL + ";");
					}
					contentPosition += height;
					mxGeometry g = cellLine.getGeometry();
					g.setTerminalPoint(new mxPoint(0, g.getHeight() / 2), false);
					g.setTerminalPoint(new mxPoint(g.getWidth(), g.getHeight() / 2), true);
					cellLine.setGeometry(g);
				} else if (element instanceof GroupModel) {
					GroupModel ele = (GroupModel) element;
					String name = ele.getName();
					if (ele.getRef() != null) {
						name = "(" + ele.getRef().getLocalPart() + ")";
					}
					GroupGraph eg = new GroupGraph(ele, getGraph());
					mxCell cellLine = eg.createGroupCell(parent, name, 0, getContentPosition(),
							widths[0] + widths[1] + 100 - 6, height, widths);
					if (iconParent != null) {
						getGraph().insertEdge(getGraph().getDefaultParent(), "", "", iconParent, cellLine,
								mxConstants.STYLE_EDGE + "=" + mxConstants.EDGESTYLE_ORTHOGONAL + ";");
					}

					mxGeometry g = cellLine.getGeometry();
					contentPosition += g.getHeight();
					g.setX(x);
					g.setTerminalPoint(new mxPoint(0, g.getHeight() / 2), false);
					g.setTerminalPoint(new mxPoint(g.getWidth(), g.getHeight() / 2), true);
					cellLine.setGeometry(g);
					parent.insert(cellLine);
				}
			} else {
				mxCell iCell = null;
				int move = 0;
				if (iconParent != null) {
					move = ((int) iconParent.getGeometry().getX()) + height + (height / 2);
				}
				iCell = insertIcon(parent, element.getContentType().toLowerCase(), height, move);
				if (iconParent != null) {
					getGraph().insertEdge(getGraph().getDefaultParent(), "", "", iconParent, iCell,
							mxConstants.STYLE_EDGE + "=" + mxConstants.EDGESTYLE_ORTHOGONAL + ";");
				}
				apppendContent(parent, iCell, element.getElements(), widths, height);
			}
		}
	}

	private void apppendContent(mxCell parent, Object iconParent, List<AttributeModel> attributes, int[] widths,
			int height) {
		int x = height + (height / 2);
		for (AttributeModel attribute : attributes) {
			AttributeGraph eg = new AttributeGraph(attribute, getGraph());
			mxCell cellLine = eg.createAttributeLine(parent, x, getContentPosition(), widths[0], height, widths[1]);
			mxCell cellType = inserType(attribute);
			if (isAddType()) {
				addCellType(cellType);
				getGraph().insertEdge(getGraph().getDefaultParent(), "", "", cellLine, cellType,
						mxConstants.STYLE_EDGE + "=" + mxConstants.EDGESTYLE_ORTHOGONAL + ";");
			}
			contentPosition += height;
			mxGeometry g = cellLine.getGeometry();
			g.setTerminalPoint(new mxPoint(0, g.getHeight() / 2), false);
			g.setTerminalPoint(new mxPoint(g.getWidth(), g.getHeight() / 2), true);
			cellLine.setGeometry(g);
		}
		if (attributes.size() > 0)
			contentPosition += 3;
	}

	private void apppendContentAttributeGroup(mxCell parent, Object iconParent, List<AttributeGroupModel> attributes,
			int[] widths, int height) {
		int x = height + (height / 2);
		for (AttributeGroupModel attribute : attributes) {
			String name = attribute.getName();
			if (attribute.getRef() != null) {
				name = "(" + attribute.getRef().getLocalPart() + ")";
			}
			AttributeGroupGraph eg = new AttributeGroupGraph(attribute, getGraph());
			mxCell cellLine = eg.crateAttributeGroupCell(parent, name, 0, getContentPosition(),
					widths[0] + widths[1] + 100 - 6, height, widths);
			cellLine.setStyle(mxConstants.STYLE_STROKECOLOR + "=GREEN" + cellLine.getStyle());
			mxGeometry g = cellLine.getGeometry();
			contentPosition += g.getHeight();
			g.setX(x);
			g.setTerminalPoint(new mxPoint(0, g.getHeight() / 2), false);
			g.setTerminalPoint(new mxPoint(g.getWidth(), g.getHeight() / 2), true);
			cellLine.setGeometry(g);
			parent.insert(cellLine);
		}
		if (attributes.size() > 0)
			contentPosition += 3;

	}

	private mxCell inserType(ElementModel element) {
		mxCell type = null;
		if (isAddType()) {
			mxCell parent = typeGroup;
			int x = 0;
			int y = (int) typeGroup.getGeometry().getHeight();
			y += 21;

			if (element.getSimpleType() != null) {
				String value = element.getSimpleType().getName();
				if (value == null || value.isEmpty()) {
					value = "(" + element.getName() + Constants.SUFFIX_SIMPLETYPE + ")";
				}
				type = new SimpleTypeGraph(element.getSimpleType(), getGraph()).createSimpleType(parent, value, x, y);

			} else if (element.getComplexType() != null) {
				String value = element.getComplexType().getName();
				if (value == null || value.isEmpty()) {
					value = "(" + element.getName() + Constants.SUFFIX_COMPLEXTYPE + ")";
				}
				type = new ComplexTypeGraph(element.getComplexType(), getGraph()).createComplexTypeCell(parent, value,
						x, y);
			} else if (element.getType() != null) {
				type = createType(parent, element.getType().getLocalPart(), x, y);
			}
			if (type != null) {
				typeGroup.getGeometry().setHeight(y + type.getGeometry().getHeight());
			}
		}
		return type;
	}

	private mxCell inserType(AttributeModel attribute) {
		mxCell type = null;
		if (isAddType()) {
			mxCell parent = typeGroup;
			int x = 0;
			int y = (int) typeGroup.getGeometry().getHeight();
			y += 21;

			if (attribute.getSimpleType() != null) {
				String value = attribute.getSimpleType().getName();
				if (value == null || value.isEmpty()) {
					value = "(" + attribute.getName() + Constants.SUFFIX_SIMPLETYPE + ")";
				}
				type = new SimpleTypeGraph(attribute.getSimpleType(), getGraph()).createSimpleType(parent, value, x, y);

			} else if (attribute.getType() != null) {
				type = createType(parent, attribute.getType().getLocalPart(), x, y);
			}
			if (type != null) {
				typeGroup.getGeometry().setHeight(y + type.getGeometry().getHeight());
			}
		}
		return type;
	}

	private int[] getSizes() {
		int[] sizes = { 0, 0 };
		int[] tempSizes = getSizesG(complexType.getAttributeGroups());
		if (tempSizes[0] > sizes[0]) {
			sizes[0] = tempSizes[0];
		}
		if (tempSizes[1] > sizes[1]) {
			sizes[1] = tempSizes[1];
		}
		tempSizes = getSizes(complexType.getAttributes());
		if (tempSizes[0] > sizes[0]) {
			sizes[0] = tempSizes[0];
		}
		if (tempSizes[1] > sizes[1]) {
			sizes[1] = tempSizes[1];
		}
		tempSizes = getSizes(complexType.getElements());
		if (tempSizes[0] > sizes[0]) {
			sizes[0] = tempSizes[0];
		}
		if (tempSizes[1] > sizes[1]) {
			sizes[1] = tempSizes[1];
		}
		return sizes;
	}

	private int[] getSizes(ArrayList<IComplexContentModel> elements) {
		int[] sizes = { 0, 0 };
		if (elements != null) {
			for (IComplexContentModel element : elements) {
				int[] tempSizes = { 0, 0 };
				if (element instanceof AttributeGroupModel) {
					tempSizes = getSizes((AttributeGroupModel) element);
				} else if (!(element instanceof ElementModel)) {
					addIconColumn();
					tempSizes = getSizes(element.getElements());
				} else {
					tempSizes = getSize((ElementModel) element);
				}
				if (tempSizes[0] > sizes[0]) {
					sizes[0] = tempSizes[0];
				}
				if (tempSizes[1] > sizes[1]) {
					sizes[1] = tempSizes[1];
				}
			}
		}
		return sizes;
	}

	private int[] getSizesG(ArrayList<AttributeGroupModel> elements) {
		int[] sizes = { 0, 0 };
		if (elements != null) {
			for (AttributeGroupModel element : elements) {
				int[] tempSizes = { 0, 0 };
				tempSizes = getSizes(element);
				if (tempSizes[0] > sizes[0]) {
					sizes[0] = tempSizes[0];
				}
				if (tempSizes[1] > sizes[1]) {
					sizes[1] = tempSizes[1];
				}
			}
		}
		return sizes;
	}

	private void addIconColumn() {
		iconsColumn++;
	}

	private int[] getSizes(AttributeGroupModel attributeGroup) {
		int[] sizes = { 0, 0 };
		int[] tempSizes = getSizes(attributeGroup.getAttributes());
		if (tempSizes[0] > sizes[0]) {
			sizes[0] = tempSizes[0];
		}
		if (tempSizes[1] > sizes[1]) {
			sizes[1] = tempSizes[1];
		}
		return sizes;
	}

	private int[] getSizes(List<AttributeModel> attributes) {
		int[] sizes = { 0, 0 };
		if (attributes != null) {
			for (AttributeModel atr : attributes) {
				int[] tempSizes = getSizes(atr);
				if (tempSizes[0] > sizes[0]) {
					sizes[0] = tempSizes[0];
				}
				if (tempSizes[1] > sizes[1]) {
					sizes[1] = tempSizes[1];
				}
			}
		}
		return sizes;
	}

	private int[] getSize(ElementModel element) {
		int[] sizes = { 0, 0 };
		int iconSize = (int) GraphTools.getTextSize(element.getName()).getHeight();
		sizes[0] = (int) GraphTools.getTextSize(element.getName()).getWidth() + iconSize + (iconSize / 2);

		String value = "";
		if (element.getSimpleType() != null) {
			value = element.getSimpleType().getName();
			if (value == null || value.isEmpty()) {
				value = "(" + element.getName() + Constants.SUFFIX_SIMPLETYPE + ")";
			}
		} else if (element.getComplexType() != null) {
			value = element.getComplexType().getName();
			if (value == null || value.isEmpty()) {
				value = "(" + element.getName() + Constants.SUFFIX_COMPLEXTYPE + ")";
			}
		} else if (element.getType() != null) {
			value = element.getType().getLocalPart();
		}
		sizes[1] = (int) GraphTools.getTextSize(value).getWidth() + iconSize + (iconSize / 2);
		return sizes;
	}

	private int[] getSizes(AttributeModel atr) {
		int[] sizes = { 0, 0 };
		int iconSize = (int) GraphTools.getTextSize(atr.getName()).getHeight();
		sizes[0] = (int) GraphTools.getTextSize(atr.getName()).getWidth() + iconSize + (iconSize / 2);
		String value = "";
		if (atr.getType() != null) {
			value = atr.getType().getLocalPart();
		} else if (atr.getSimpleType() != null) {
			value = atr.getSimpleType().getName();
			if (value == null || value.isEmpty()) {
				value = "(" + atr.getName() + Constants.SUFFIX_SIMPLETYPE + ")";
			}
		}
		sizes[1] = (int) GraphTools.getTextSize(value).getWidth() + iconSize + (iconSize / 2);
		return sizes;
	}

	protected int getIconsColumn() {
		return iconsColumn;
	}

	protected List<mxCell> getCellTypes() {
		return cellTypes;
	}

	protected Object[] getCellTypesArray() {
		if (cellTypes == null)
			cellTypes = new ArrayList<mxCell>();
		return cellTypes.toArray();
	}

	protected void addCellType(mxCell cell) {
		if (getCellTypes() == null)
			cellTypes = new ArrayList<mxCell>();
		typeGroup.insert(cell);
		getCellTypes().add(cell);
	}

	@Override
	protected mxCell insertIcon(mxCell parent, String icon, int size) {

		return insertIcon(parent, icon, size, 0);
	}

	private mxCell insertIcon(mxCell parent, String icon, int size, int move) {
		mxCell icono = super.insertIcon(parent, icon, size);
		mxGeometry g = icono.getGeometry();
		if (move > 0) {
			g.setX(size + move);
			g.setY(getContentPosition());
			icono.setId(GraphConstants.EXCLUDE_PREFIX_ICON + parent.getId() + move);
		}

		g.setTerminalPoint(new mxPoint(0, g.getHeight() / 2), false);
		g.setTerminalPoint(new mxPoint(g.getWidth(), g.getHeight() / 2), true);
		icono.setGeometry(g);

		return icono;

	}

	protected int getContentPosition() {
		return contentPosition;
	}

	protected boolean isAddType() {
		return addType;
	}

}
