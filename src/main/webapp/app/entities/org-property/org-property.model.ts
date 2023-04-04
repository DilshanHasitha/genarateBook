export interface IOrgProperty {
  id: number;
  name?: string | null;
  description?: string | null;
  isActive?: boolean | null;
}

export type NewOrgProperty = Omit<IOrgProperty, 'id'> & { id: null };
