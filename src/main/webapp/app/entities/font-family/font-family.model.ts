export interface IFontFamily {
  id: number;
  name?: string | null;
  url?: string | null;
  isActive?: boolean | null;
}

export type NewFontFamily = Omit<IFontFamily, 'id'> & { id: null };
