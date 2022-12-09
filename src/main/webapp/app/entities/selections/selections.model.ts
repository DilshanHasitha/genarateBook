export interface ISelections {
  id: number;
  avatarCode?: string | null;
  styleCode?: string | null;
  optionCode?: string | null;
  image?: string | null;
  height?: number | null;
  x?: number | null;
  y?: number | null;
  isActive?: boolean | null;
  width?: number | null;
  avatarAttributesCode?: string | null;
}

export type NewSelections = Omit<ISelections, 'id'> & { id: null };
