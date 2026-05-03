import { useState, useEffect } from "react";

export default function useRecommendForm() {
  const [step, setStep] = useState(0);
  const [formData, setFormData] = useState({});
  const [cats, setCats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [accessToken, setAccessToken] = useState(null);

  useEffect(() => {
    const fetchCategories = async () => {
    try {
      const refreshRes = await fetch("https://test-fin.duckdns.org/auth/refresh", {
        method: "POST",
        credentials: "include",
      });

      const refreshData = await refreshRes.json();
      const token = refreshData.data;
      setAccessToken(token);

      const res = await fetch("https://test-fin.duckdns.org/api/categories", {
        headers: { Authorization: `Bearer ${token}` },
      });

      if (!res.ok) throw new Error("카테고리 요청 실패");

      const data = await res.json();

      setCats({
        regions: data.find((c) => c.categoryName === "거주지역")?.options || [],
        status: data.find((c) => c.categoryName === "현재신분")?.options || [],
        savingPeriod: data.find((c) => c.categoryName === "저축기간")?.options || [],
        benefits: data.find((c) => c.categoryName === "핵심혜택")?.options || [],
        bankRelation: data.find((c) => c.categoryName === "은행거래")?.options || [],
        banks: ["은행임시1", "은행임시2", "은행임시3"],
        incomeLevel: [
          { label: "중위소득 60%", amount: "월 154만원 이하" },
          { label: "중위소득 80%", amount: "월 205만원 이하" },
          { label: "중위소득 100%", amount: "월 256만원 이하" },
          { label: "중위소득 120%", amount: "월 308만원 이하" },
          { label: "중위소득 150%", amount: "월 385만원 이하" },
          { label: "중위소득 180%", amount: "" },
        ],
      });
    } catch (e) {
      console.error("카테고리 불러오기 실패:", e);
    } finally {
      setLoading(false);
    }
  };

  fetchCategories();
  }, []);
  
  // 서버에 보내기는 나중에...
  const handleSubmit = async () => {
    /*
    try {
    const res = await fetch(“https://test-fin.duckdns.org/api/recommend”, {
    method: “POST”,
    headers: {
    “Content-Type”: “application/json”,
    Authorization: `Bearer ${accessToken}`,
    },
    body: JSON.stringify(formData),
    });
    if (!res.ok) throw new Error(“전송 실패”);
    const result = await res.json();
    console.log(“추천 결과:”, result);
    } catch (e) {
    console.error(e);
    }
    */
  };

  const go = (n) => () => setStep(n);

  return { step, formData, setFormData, cats, loading, go, handleSubmit };
}